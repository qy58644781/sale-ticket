package com.yadan.saleticket.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.base.tools.LocalDateTimeDeserializer;
import com.yadan.saleticket.base.tools.LocalDateTimeSerializer;
import com.yadan.saleticket.model.product.ProductDetail;
import com.yadan.saleticket.model.theatre.Hall;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductDetailVo {

    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;
    /**
     * 演出场次
     */
    private Integer times;

    /**
     * 所属产品Id
     */
    private ProductVo productVo;

    /**
     * 所属放映厅
     */
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Hall hall;

    /**
     * 开始时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    private List<ProductPriceVo> productPriceVos;

    public static ProductDetailVo from(ProductDetail productDetail) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyNotNullProperties(productDetail, productDetailVo);
        productDetailVo.setProductPriceVos(ProductPriceVo.from(productDetail.getProductPrices()));
        return productDetailVo;
    }

    public static List<ProductDetailVo> from(List<ProductDetail> productDetails) {
        List<ProductDetailVo> vos = new ArrayList<>();;
        if(CollectionUtils.isNotEmpty(productDetails)) {
            for (ProductDetail productDetail : productDetails){
                vos.add(ProductDetailVo.from(productDetail));
            }
        }
        return vos;
    }
}
