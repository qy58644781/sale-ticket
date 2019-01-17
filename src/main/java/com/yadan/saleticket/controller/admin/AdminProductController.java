package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.security.SecurityService;
import com.yadan.saleticket.base.tools.Json;
import com.yadan.saleticket.dao.hibernate.ProductDetailRepository;
import com.yadan.saleticket.dao.hibernate.ProductPriceRepository;
import com.yadan.saleticket.dao.hibernate.ProductRepository;
import com.yadan.saleticket.dao.hibernate.base.STPageRequest;
import com.yadan.saleticket.entity.PageVo;
import com.yadan.saleticket.entity.product.AddProductVo;
import com.yadan.saleticket.entity.product.ProductVo;
import com.yadan.saleticket.enums.ApproveStatusEnum;
import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.product.Product;
import com.yadan.saleticket.model.product.ProductDetail;
import com.yadan.saleticket.service.ProductService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    private SecurityService securityService;

    @GetMapping("")
    public PageVo<ProductVo> products(STPageRequest pageRequest, String filter) {
        Map<String, String> parse = Json.String2Object(filter, HashMap.class);
        Page<Product> products;
        if (MapUtils.isNotEmpty(parse)) {
            products = productRepository.findAllByFilterAndPageRequest(pageRequest, parse, Product.class);
        } else {
            products = productRepository.findAll(pageRequest.genPageRequest());
        }
        return new PageVo<>(ProductVo.from(products.getContent()), products.getTotalElements());
    }

    @GetMapping("/{id}")
    public ProductVo product(@PathVariable("id") Long id) {
        return ProductVo.from(productRepository.findOne(id));
    }

    @PostMapping("/merge")
    public ProductVo merge(@RequestBody AddProductVo vo) {
        Product product = productService.createProduct(vo);
        return ProductVo.from(product);
    }

    @PostMapping("/approve")
    public void approve(Long productId) {
        Product product = productRepository.findOne(productId);
        if(ApproveStatusEnum.pass(product.getApproveStatusEnum())) {
            product.setApproveStatusEnum(ApproveStatusEnum.UNPASSED);
        } else {
            product.setApproveStatusEnum(ApproveStatusEnum.PASSED);
        }
        product.setApprover(securityService.getCurrentLoginUser());
        productRepository.merge(product);
    }

    @GetMapping("/exportSeatPriceExcel")
    public void exportSeatPriceExcel(Long productDetailId, HttpServletResponse response) throws IOException {
        ProductDetail productDetail = productDetailRepository.findOne(productDetailId);
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

    @PostMapping("/detail/delete")
    @Transactional
    public Long deleteProductDetail(Long id) {
        ProductDetail productDetail = productDetailRepository.findOne(id);
        Product product = productDetail.getProduct();
        if(product.getApproveStatusEnum().equals(ApproveStatusEnum.PASSED)) {
            throw new ServiceException(ExceptionCode.INVALID_PRODUCT, "审核通过的产品无法修改");
        }
        productDetail.getProductPrices().forEach(each->{
            productPriceRepository.delete(each);
        });
        productDetailRepository.delete(productDetail);
        return id;
    }

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

    @PostMapping("/delete")
    @Transactional
    public Set<Long> delete(String ids) {
        Set<Long> result = new HashSet<>();
        return result;
    }
}
