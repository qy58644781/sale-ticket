package com.yadan.saleticket.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddOrderVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 下单用户id
     */
    private Long userId;

    /**
     * 是否在线选座
     */
    private Boolean onlineSale;

    /**
     * 购买多张票时候的明细
     */
    private List<AddOrderDetailVo> addOrderDetailVos;
}
