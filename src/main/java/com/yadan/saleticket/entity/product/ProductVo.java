package com.yadan.saleticket.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.base.tools.LocalDateTimeDeserializer;
import com.yadan.saleticket.base.tools.LocalDateTimeSerializer;
import com.yadan.saleticket.enums.ApproveStatusEnum;
import com.yadan.saleticket.model.product.Product;
import com.yadan.saleticket.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductVo {

    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 名称
     */
    private String name;

    /**
     * 详情
     */
    private String description;

    /**
     * 是否在线选座
     */
    private Boolean onlineSale;

    /**
     * 产品海报图
     */
    private String pic;

    /**
     * 产品视频
     */
    private String video;

    /**
     * 演出时长（分钟）
     */
    private Integer length;

    /**
     * 审核状态
     */
    private ApproveStatusEnum approveStatusEnum;

    /**
     * 创建人
     */
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User creater;

    /**
     * 修改人
     */
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User updater;

    /**
     * 审核人
     */
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User approver;

    private List<ProductDetailVo> productDetailVos;

    public static ProductVo from(Product product) {
        ProductVo productVo = new ProductVo();
        BeanUtils.copyNotNullProperties(product, productVo);
        productVo.setProductDetailVos(ProductDetailVo.from(product.getProductDetails()));
        return productVo;
    }

    public static List<ProductVo> from(List<Product> products) {
        List<ProductVo> vos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(products)) {
            for (Product product : products) {
                vos.add(ProductVo.from(product));
            }
        }
        return vos;
    }
}
