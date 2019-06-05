package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.http.STResponse;
import com.yadan.saleticket.base.http.handler.JSONFilter;
import com.yadan.saleticket.base.security.SecurityService;
import com.yadan.saleticket.dao.hibernate.ProductDetailRepository;
import com.yadan.saleticket.dao.hibernate.ProductPriceRepository;
import com.yadan.saleticket.dao.hibernate.ProductRepository;
import com.yadan.saleticket.dao.hibernate.base.STPageRequest;
import com.yadan.saleticket.dao.redis.RedisKeyPrefix;
import com.yadan.saleticket.dao.redis.RedisLock;
import com.yadan.saleticket.entity.PageVo;
import com.yadan.saleticket.entity.product.AddProductVo;
import com.yadan.saleticket.enums.ApproveStatusEnum;
import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.product.Product;
import com.yadan.saleticket.model.product.ProductDetail;
import com.yadan.saleticket.service.product.ProductRedisService;
import com.yadan.saleticket.service.product.ProductService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductRedisService productRedisService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private SecurityService securityService;

    private static final JSONFilter jsonFilter = new JSONFilter(new String[]{
            "*.approver.nickname",
            "*.theatre.name",
            "*.hall.name",
    }, new String[]{
            "*.approver.*", "*.creater.*", "*.updater.*",
            "*.theatre.*",
            "*.hall.*",
            "*.productPrices"
    });

    @GetMapping("")
    public STResponse<PageVo> products(STPageRequest pageRequest) {
//        PageVo<Product> products = productRepository.findAllByFilterAndPageRequest(pageRequest);
//        return new STResponse<>(products, jsonFilter);
        return null;
    }

    @GetMapping("/{id}")
    public STResponse<Product> product(@PathVariable("id") Long id) {
        return new STResponse<>(productRepository.getOne(id), jsonFilter);
    }

    @PostMapping("/merge")
    public STResponse<Product> merge(@RequestBody AddProductVo vo) {
        Product product = productService.mergeProduct(vo);
        return new STResponse<>(product, jsonFilter);
    }

    @PostMapping("/delete")
    @Transactional
    public Set<Long> delete(String ids) {
        Set<Long> result = new HashSet<>();
        String[] split = ids.split(",");
        if (split != null && split.length > 0) {
            for (String id : split) {
                Product product = productRepository.getOne(Long.valueOf(id));
                productRepository.delete(product);
                result.add(Long.valueOf(id));
            }
        }
        return result;
    }

    /**
     * 审核
     * @param productId
     */
    @PostMapping("/approve")
    public void approve(Long productId) {
        redisLock.lock(() -> {
            Product product = productRepository.getOne(productId);
            if (ApproveStatusEnum.pass(product.getApproveStatusEnum())) {
                product.setApproveStatusEnum(ApproveStatusEnum.UNPASSED);
                productRedisService.removeProductCache(product);

            } else {
                product.setApproveStatusEnum(ApproveStatusEnum.PASSED);
                productRedisService.createProductCache(product);
            }
            product.setApprover(securityService.getCurrentLoginUser());
            product.setUpdater(securityService.getCurrentLoginUser());
            productRepository.save(product);
            return null;
        }, RedisKeyPrefix.LOCK.getPrefix() + RedisKeyPrefix.PRODUCT_DETAIL_KEY.getPrefix() + productId, 200, 100, 1000);

    }

    /**
     * 导出每个座位的价格
     * @param productDetailId
     * @param response
     * @throws IOException
     */
    @GetMapping("/exportSeatPriceExcel")
    public void exportSeatPriceExcel(Long productDetailId, HttpServletResponse response) throws IOException {
        ProductDetail productDetail = productDetailRepository.getOne(productDetailId);
        Boolean onlineSale = productDetail.getProduct().getOnlineSale();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (onlineSale) {
            productService.createProductPriceOnlineExcel(productDetail).write(os);
        } else {
            productService.createProductPriceOfflineExcel(productDetail).write(os);
        }
        byte[] content = os.toByteArray();
        response.setContentLength(content.length);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((productDetail.getProduct().getName() + ".xlsx").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        StreamUtils.copy(content, response.getOutputStream());
        out.close();
        response.flushBuffer();
    }

    /**
     * 删除场次
     * @param id
     * @return
     */
    @PostMapping("/detail/delete")
    @Transactional
    public Long deleteProductDetail(Long id) {
        ProductDetail productDetail = productDetailRepository.getOne(id);
        Product product = productDetail.getProduct();
        if (product.getApproveStatusEnum().equals(ApproveStatusEnum.PASSED)) {
            throw new ServiceException(ExceptionCode.INVALID_PRODUCT, "审核通过的产品无法修改");
        }
        productDetail.getProductPrices().forEach(each -> {
            productPriceRepository.delete(each);
        });
        productDetailRepository.delete(productDetail);
        return id;
    }

    /**
     * 票类型枚举
     * @return
     */
    @GetMapping("/ticketTypeEnum")
    public List<Map> hallEnums() {
        List<Map> result = new ArrayList<>();
        for (TicketTypeEnum each : TicketTypeEnum.values()) {
            Map map = new HashMap();
            map.put("id", each);
            map.put("name", each.getVal());
            result.add(map);
        }
        return result;
    }

    /**
     * 审核枚举
     * @return
     */
    @GetMapping("/approveStatusEnum")
    public List<Map> approveStatusEnum() {
        List<Map> result = new ArrayList<>();
        for (ApproveStatusEnum each : ApproveStatusEnum.values()) {
            Map map = new HashMap();
            map.put("id", each);
            map.put("name", each.getVal());
            result.add(map);
        }
        return result;
    }


}
