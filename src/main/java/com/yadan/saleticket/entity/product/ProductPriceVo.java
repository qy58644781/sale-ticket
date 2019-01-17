package com.yadan.saleticket.entity.product;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.base.tools.LocalDateTimeDeserializer;
import com.yadan.saleticket.base.tools.LocalDateTimeSerializer;
import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.product.ProductPrice;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductPriceVo {

    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    private ProductDetailVo productDetailVo;

    /**
     * 库存（非在线选座的时候是手动输入，在线选择是根据座位求和）
     */
    private Integer inventory;

    /**
     * 从第几排开始
     * 用于（非在线选座）
     */
    private Integer seatFrom;

    /**
     * 到第几排结束
     * 用于（非在线选座）
     */
    private Integer seatTo;

    /**
     * 区域名称
     * 用于（非在线选座）
     */
    private String areaName;

//    /**
//     * 座位
//     * 用于（在线选座）
//     */
//    private List<Seat> seats;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 票类型
     */
    private TicketTypeEnum ticketTypeEnum;

    public static ProductPriceVo from(ProductPrice productPrice) {
        ProductPriceVo productPriceVo = new ProductPriceVo();
        BeanUtils.copyNotNullProperties(productPrice, productPriceVo);
        return productPriceVo;
    }

    public static List<ProductPriceVo> from(List<ProductPrice> productPrices) {
        List<ProductPriceVo> vos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(productPrices)) {
            for (ProductPrice productPrice : productPrices) {
                vos.add(ProductPriceVo.from(productPrice));
            }
        }
        return vos;
    }
}
