package com.yadan.saleticket.entity.order;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单明细vo，针对一次下单购买多个座位
 */
@Getter
@Setter
public class AddOrderDetailVo {

    /**
     * 价格id
     */
    private Long productPriceId;

    /**
     * 如果是在线选座的，需要选择座位id
     */
    private Long seatId;

    /**
     * 如果是非在线选座的，需要填写购买数量
     */
    private Integer number;
}
