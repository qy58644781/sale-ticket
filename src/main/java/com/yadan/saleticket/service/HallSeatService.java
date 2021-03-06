package com.yadan.saleticket.service;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.base.tools.ExcelUtils;
import com.yadan.saleticket.dao.hibernate.HallRepository;
import com.yadan.saleticket.dao.hibernate.SeatRepository;
import com.yadan.saleticket.dao.hibernate.TheatreRepository;
import com.yadan.saleticket.entity.AddHallVo;
import com.yadan.saleticket.entity.HallSeatsVo;
import com.yadan.saleticket.model.theatre.Hall;
import com.yadan.saleticket.model.theatre.Seat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.yadan.saleticket.base.exception.ExceptionCode.INVALID_SEAT;

@Service
@Slf4j
public class HallSeatService {

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    /**
     * 根据剧院座位生成list列表
     *
     * @param hallId
     * @return
     */
    public HallSeatsVo genHallSeatVo(Long hallId) {
        HallSeatsVo vo = new HallSeatsVo();
        return vo;
    }

    @Transactional
    public Hall mergeHall(AddHallVo addHallVo) {
        if (addHallVo.getId() == null && addHallVo.getSeatFile() == null) {
            throw new ServiceException(ExceptionCode.INVALID_SEAT, "必须上传座位排位信息");
        }
        Hall saveHall;
        if (addHallVo.getId() != null) {
            saveHall = hallRepository.getOne(addHallVo.getId());
        } else {
            saveHall = new Hall();
            saveHall.setTheatre(theatreRepository.getOne(addHallVo.getTheatreId()));
            createSeatByExcel(addHallVo.getSeatFile().getStream(), saveHall);
        }
        BeanUtils.copyNotNullProperties(addHallVo, saveHall);
        hallRepository.save(saveHall);

        return saveHall;
    }

    /**
     * 根据excel创建剧院座位
     */
    @Transactional
    public void createSeatByExcel(InputStream is, Hall hall) {

        log.debug("开始导入 hallId: " + hall.getId() + " 的excel");

        XSSFWorkbook excel = null;
        // 最大列
        short maxColumn = 0;
        // 最大行
        int maxRow = 0;

        List<Seat> saveSeats = new ArrayList<>();

        try {
            excel = new XSSFWorkbook(is);
            XSSFSheet sheet = excel.getSheetAt(0);

            int lastLineIndex = sheet.getLastRowNum();
            if (lastLineIndex < 0) {
                throw new ServiceException(ExceptionCode.INVALID_EXCEL_DATA, "模板数据有误，第一个sheet的内容为空");
            }
            maxRow = lastLineIndex + 1;

            for (int i = 0; i <= lastLineIndex; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                short lastCellNum = row.getLastCellNum();
                if (lastCellNum > maxColumn) maxColumn = lastCellNum;

                if (lastCellNum <= 0) {
                    continue;
                }
                for (int j = 0; j < lastCellNum; j++) {
                    XSSFCell cell = row.getCell(j);

                    Object value = ExcelUtils.getValue(cell);
                    if (value == null) {
                        continue;
                    }
                    String[] split = value.toString().split(":");
                    if (split.length != 3) {
                        log.error("x: " + i + ", y: " + j + ", val: " + value + "|");
                        throw new ServiceException(INVALID_SEAT, "excel中座位信息填写有误");
                    }

                    if (value != null) {
                        Seat seat = new Seat();
                        seat.setValid(true);
                        seat.setAreaName(split[0]);
                        seat.setSeatRow(Integer.valueOf(split[1]));
                        seat.setSeatColumn(Integer.valueOf(split[2]));
                        seat.setSiteRow(i);
                        seat.setSiteColumn(j);
                        seat.setHall(hall);
                        saveSeats.add(seat);

                        log.debug("x: " + i + ", y: " + j + ", val: " + value + "|");
                    }
                }
            }

            // 更新座位
            seatRepository.saveAll(saveSeats);

            hall.setMaxColumn(maxRow);
            hall.setMaxRow(maxRow);
            hallRepository.save(hall);

            log.debug("导入 hallId: " + hall.getId() + " 的excel座位成功");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ExcelUtils.close(excel);
            ExcelUtils.close(is);
        }
    }

    /**
     * 根据座位生成剧院
     *
     * @param hall
     * @return
     */
    public Workbook createExcelBySeat(Hall hall) {

        List<Seat> seats = seatRepository.findAllByHall(hall);
        if (CollectionUtils.isEmpty(seats)) {
            throw new ServiceException(INVALID_SEAT, "获取剧院座位数据失败");
        }
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(hall.getName());

        int tempLine = -1;
        int maxColumn = 0;
        Row row = null;
        for (int i = 0; i < seats.size(); i++) {
            Seat each = seats.get(i);
            log.debug("第" + i + "个座位，x=" + each.getSiteRow() + " y=" + each.getSiteColumn());
            if (each.getSiteRow() > tempLine) {
                row = sheet.createRow(each.getSiteRow());
                tempLine = each.getSiteRow();
                log.debug("====开始创建第" + tempLine + "行数据====");
            }
            if (each.getSiteColumn() > maxColumn) {
                maxColumn = each.getSiteColumn();
            }
            Cell cell = row.createCell(each.getSiteColumn());
            cell.setCellValue(each.getAreaName() + ":" + each.getSeatRow() + ":" + each.getSeatColumn());
            cell.setCellStyle(ExcelUtils.genCellStyle(wb));
        }

        // 设置列宽
        for (int i = 0; i < maxColumn; i++) {
            sheet.setColumnWidth((short) i, (short) (13 * 150));
        }

        return wb;
    }

    /**
     * 生成固定的模板
     *
     * @return
     */
    public Workbook createExcelForOffline() {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("线下导入模板");
        Row keyRow = sheet.createRow(0);

        CellStyle style = ExcelUtils.genCellStyle(wb);

        Cell cell0 = keyRow.createCell(0);
        cell0.setCellValue("区域名称");
        cell0.setCellStyle(style);

        Cell cell1 = keyRow.createCell(1);
        cell1.setCellValue("从第几排");
        cell1.setCellStyle(style);

        Cell cell2 = keyRow.createCell(2);
        cell2.setCellValue("到第几排");
        cell2.setCellStyle(style);

        Cell cell3 = keyRow.createCell(3);
        cell3.setCellValue("价格");
        cell3.setCellStyle(style);

        Cell cell4 = keyRow.createCell(4);
        cell4.setCellValue("库存");
        cell4.setCellStyle(style);

        Cell cell5 = keyRow.createCell(5);
        cell5.setCellStyle(style);
        cell5.setCellValue("票类型：普通（不填表示默认）；赠品；员工");

        for (int i = 0; i < 5; i++) {
            sheet.setColumnWidth((short) i, (short) (25 * 150));
        }
        sheet.setColumnWidth((short) 5, (short) (75 * 150));
        return wb;
    }

}
