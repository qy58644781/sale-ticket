package com.yadan.saleticket.service;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.security.SecurityService;
import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.base.tools.ExcelUtils;
import com.yadan.saleticket.dao.hibernate.*;
import com.yadan.saleticket.entity.*;
import com.yadan.saleticket.enums.ApproveStausEnum;
import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.Product.Product;
import com.yadan.saleticket.model.Product.ProductDetail;
import com.yadan.saleticket.model.Product.ProductPrice;
import com.yadan.saleticket.model.Theatre.Hall;
import com.yadan.saleticket.model.Theatre.Seat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SeatRepository seatRepository;

    @Transactional
    public Product createProduct(AddProductVo addProductVo) {

        if (CollectionUtils.isEmpty(addProductVo.getProductDetails())) {
            throw new ServiceException(ExceptionCode.INVALID_ADD_PRODUCT_VO, "必须输入票务明细");
        }

        // 新建产品
        Product savedProduct = new Product();
        savedProduct.setApproveStausEnum(ApproveStausEnum.TBD);
        savedProduct.setCreater(securityService.getCurrentLoginUser());
        BeanUtils.copyNotNullProperties(addProductVo, savedProduct);
        productRepository.save(savedProduct);

        for (AddProductDetailVo addProductDetailVo : addProductVo.getProductDetails()) {
            if (CollectionUtils.isEmpty(addProductDetailVo.getStartTimes())) {
                throw new ServiceException(ExceptionCode.INVALID_ADD_PRODUCT_VO, "必须输入票务场次信息");
            }
            for (AddProductTimeVo productTimeVo : addProductDetailVo.getStartTimes()) {
                // 新建产品明细
                ProductDetail savedProductDetail = createProductDetail(productTimeVo, savedProduct, addProductDetailVo.getHallId());

                // 新建产品价格
                if (addProductVo.getOnlineSale()) {
                    createOnlineProductPrices(addProductDetailVo, savedProductDetail);
                } else {
                    createOfflineProductPrices(addProductVo, addProductDetailVo, savedProductDetail);
                }
            }
        }

        return savedProduct;
    }


    private ProductDetail createProductDetail(AddProductTimeVo productTimeVo, Product savedProduct, Long hallId) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProduct(savedProduct);
        Hall hall = hallRepository.findOne(hallId);
        productDetail.setHall(hall);
        productDetail.setTimes(productTimeVo.getTimes());
        productDetail.setStartTime(productTimeVo.getStartTime());
        productDetail.setEndTime(productTimeVo.getStartTime().plus(savedProduct.getLength(), ChronoUnit.MINUTES));
        productDetailRepository.save(productDetail);
        return productDetail;
    }

    private void createOnlineProductPrices(AddProductDetailVo addProductDetailVo,
                                           ProductDetail savedProductDetail) {
        if (addProductDetailVo.getPriceFile() == null) {
            throw new ServiceException(ExceptionCode.INVALID_ADD_PRODUCT_VO, "必须上传票务excel信息");
        }
        Hall hall = hallRepository.findOne(addProductDetailVo.getHallId());
        List<AddSeatPriceVo> addSeatPriceVoList = createOnlineProductSeatVoByExcelModel(hall, addProductDetailVo.getPriceFile().getStream());
        if (CollectionUtils.isEmpty(addSeatPriceVoList)) {
            throw new ServiceException(ExceptionCode.INVALID_EXCEL_DATA, "导入的excel有问题，导入失败");
        }

        Map<BigDecimal, List<AddSeatPriceVo>> seatGroupByPrice = addSeatPriceVoList.stream().collect(Collectors.groupingBy(AddSeatPriceVo::getPrice));
        for (Map.Entry<BigDecimal, List<AddSeatPriceVo>> i : seatGroupByPrice.entrySet()) {
            BigDecimal price = i.getKey();
            Map<TicketTypeEnum, List<AddSeatPriceVo>> seatGroupByTicketType = i.getValue().stream().collect(Collectors.groupingBy(AddSeatPriceVo::getTicketTypeEnum));

            for (Map.Entry<TicketTypeEnum, List<AddSeatPriceVo>> j : seatGroupByTicketType.entrySet()) {
                TicketTypeEnum type = j.getKey();
                List<AddSeatPriceVo> seats = j.getValue();
                ProductPrice productPrice = new ProductPrice();
                productPrice.setInventory(seats.size());
                productPrice.setProductDetail(savedProductDetail);
                productPrice.setSeats(seats.stream().map(each -> each.getSeat()).collect(Collectors.toList()));
                productPrice.setPrice(price);
                productPrice.setTicketTypeEnum(type);
                productPriceRepository.save(productPrice);
            }
        }
    }

    /**
     * 创建在线的座位价格对照关系
     *
     * @param hall
     * @param is
     * @return
     */
    private List<AddSeatPriceVo> createOnlineProductSeatVoByExcelModel(Hall hall, InputStream is) {
        XSSFWorkbook excel = null;
        List<AddSeatPriceVo> result = new ArrayList<>();
        try {
            excel = new XSSFWorkbook(is);

            XSSFSheet sheet = excel.getSheetAt(0);

            int lastLineIndex = sheet.getLastRowNum();
            if (lastLineIndex < 0) {
                throw new ServiceException(ExceptionCode.INVALID_EXCEL_DATA, "模板数据有误，第一个sheet的内容为空");
            }

            Map<Integer, Map<Integer, List<Seat>>> seatLocationMap = getByRowColumn(hall);

            for (int i = 0; i <= lastLineIndex; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                short lastCellNum = row.getLastCellNum();

                if (lastCellNum <= 0) {
                    continue;
                }

                // 一整行座位
                Map<Integer, List<Seat>> rowSeats = seatLocationMap.get(i);

                for (int j = 0; j < lastCellNum; j++) {
                    XSSFCell cell = row.getCell(j);
                    Object value = ExcelUtils.getValue(cell);
                    if (value != null && checkIsSeatCell(cell)) {
                        List<Seat> seats = rowSeats.get(j);
                        if (CollectionUtils.isEmpty(seats)) {
                            log.error("x: " + i + ", y: " + j + ", val: " + value);
                            throw new ServiceException(ExceptionCode.INVALID_EXCEL_DATA, "严重错误：excel中的座位位置不存在数据库中");
                        }

                        Seat seat = seats.get(0);
                        AddSeatPriceVo seatPriceVo = new AddSeatPriceVo();
                        boolean hasSplit = ((String) value).contains(":") || ((String) value).contains("：");
                        if (value instanceof String) {
                            if (hasSplit) {
                                String[] split = dealContent(value.toString());
                                seatPriceVo.setTicketTypeEnum(TicketTypeEnum.get(split[0]));
                                seatPriceVo.setPrice(new BigDecimal(split[1]));
                            } else {
                                seatPriceVo.setTicketTypeEnum(TicketTypeEnum.NORMAL);
                                seatPriceVo.setPrice(new BigDecimal(value.toString()));
                            }
                            seatPriceVo.setSeat(seat);
                        }
                        result.add(seatPriceVo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(excel);
            this.close(is);
        }
        return result;
    }

    private String[] dealContent(String content) {
        boolean hasSplit1 = ((String) content).contains(":");
        boolean hasSplit2 = ((String) content).contains("：");
        if (hasSplit1) {
            return content.split(":");
        } else if (hasSplit2) {
            return content.split("：");
        } else return null;
    }

    /**
     * 获取两级的map对象，将所有的座位根据(增加查询效率，减少for循环）
     *
     * @param hall
     * @return
     */
    private Map<Integer, Map<Integer, List<Seat>>> getByRowColumn(Hall hall) {
        Map<Integer, Map<Integer, List<Seat>>> result = new HashMap();
        List<Seat> seats = seatRepository.findAllByHallAndValid(hall, true);
        if (CollectionUtils.isEmpty(seats)) {
            throw new ServiceException(ExceptionCode.INVALID_SEAT, "hallId:" + hall.getId() + " 不存在对应的座位信息");
        }
        Map<Integer, List<Seat>> seatGroupByRow = seats.stream().collect(Collectors.groupingBy(Seat::getSeatRow));
        for (Map.Entry<Integer, List<Seat>> entry : seatGroupByRow.entrySet()) {
            List<Seat> rowSeats = entry.getValue();
            if (CollectionUtils.isEmpty(rowSeats)) {
                throw new ServiceException(ExceptionCode.INVALID_SEAT, "hallId:" + hall.getId() + " 不存在对应的座位信息");
            }
            Map<Integer, List<Seat>> seatVo = rowSeats.stream().collect(Collectors.groupingBy(Seat::getSeatColumn));
            result.put(entry.getKey(), seatVo);
        }
        return result;
    }

    /**
     * 判断是否是方框
     *
     * @param cell
     * @return
     */
    private boolean checkIsSeatCell(XSSFCell cell) {
        XSSFCellStyle cellStyle = cell.getCellStyle();
        BorderStyle b1 = cellStyle.getBorderBottomEnum();
        BorderStyle b2 = cellStyle.getBorderTopEnum();
        BorderStyle b3 = cellStyle.getBorderLeftEnum();
        BorderStyle b4 = cellStyle.getBorderRightEnum();
        if (b1 == BorderStyle.MEDIUM
                && b2 == BorderStyle.MEDIUM
                && b3 == BorderStyle.MEDIUM
                && b4 == BorderStyle.MEDIUM
                ) {
            return true;
        }
        return false;
    }

    private List<ProductPrice> createOfflineProductPrices(AddProductVo addProductVo,
                                                          AddProductDetailVo addProductDetailVo,
                                                          ProductDetail savedProductDetail) {
        if (addProductVo.getUseExcel()) {
            createOfflineProductPricesByExcel(addProductDetailVo, savedProductDetail);
        } else {
            createOfflineProductPricesByForm(addProductDetailVo, savedProductDetail);
        }
        return null;
    }

    private void createOfflineProductPricesByExcel(AddProductDetailVo addProductDetailVo,
                                                   ProductDetail savedProductDetail) {
        XSSFWorkbook excel = null;
        InputStream is = addProductDetailVo.getPriceFile().getStream();
        try {
            excel = new XSSFWorkbook(is);
            XSSFSheet sheet = excel.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <= lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                ProductPrice productPrice = new ProductPrice();
                productPrice.setProductDetail(savedProductDetail);
                productPrice.setAreaName((String) ExcelUtils.getValue(row.getCell(0), ExcelUtils.STRING));
                productPrice.setSeatFrom((Integer) ExcelUtils.getValue(row.getCell(1), ExcelUtils.INTEGER));
                productPrice.setSeatTo((Integer) ExcelUtils.getValue(row.getCell(2), ExcelUtils.INTEGER));
                productPrice.setPrice((BigDecimal) ExcelUtils.getValue(row.getCell(3), ExcelUtils.DECEMAL));
                productPrice.setInventory((Integer) ExcelUtils.getValue(row.getCell(4), ExcelUtils.INTEGER));
                Object value = ExcelUtils.getValue(row.getCell(5));
                if (null == value) {
                    productPrice.setTicketTypeEnum(TicketTypeEnum.NORMAL);
                } else {
                    productPrice.setTicketTypeEnum(TicketTypeEnum.get((String) value));
                }
                productPriceRepository.save(productPrice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(excel);
            this.close(is);
        }
    }

    private void createOfflineProductPricesByForm(AddProductDetailVo addProductDetailVo,
                                                  ProductDetail savedProductDetail) {
        if (CollectionUtils.isEmpty(addProductDetailVo.getProductPrices())) {
            throw new ServiceException(ExceptionCode.INVALID_ADD_PRODUCT_VO, "必须输入票务价格信息");
        }
        for (AddProductPriceVo productPriceVo : addProductDetailVo.getProductPrices()) {
            ProductPrice productPrice = new ProductPrice();
            BeanUtils.copyNotNullProperties(productPriceVo, productPrice);
            productPrice.setProductDetail(savedProductDetail);
            productPriceRepository.save(productPrice);
        }
    }

    private void close(Workbook wb) {
        if (wb != null) {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    private void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

}
