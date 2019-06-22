package com.yadan.saleticket.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class ProductSeatCacheVo implements Serializable {
    private Long seatId;

    private Long productPriceId;

    /**
     * 是否售出
     */
    private Boolean sell;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 对应x轴
     */
    private Integer siteRow;

    /**
     * 对应y轴
     */
    private Integer siteColumn;

    /**
     * 对应座位号X
     */
    private Integer seatRow;

    /**
     * 对应座位号Y
     */
    private Integer seatColumn;
}
