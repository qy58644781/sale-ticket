package com.yadan.saleticket.enums;

public enum OrderStatusEnum {

    /**
     * 等待客服修改
     */
    WAITIN_FOR_ADMIN,

    /**
     * 等待支付
     */
    WAITING_FOR_PAY,

    /**
     * 订单完成
     */
    COMPLETE,

    /**
     * 已退款
     */
    REFUNDED;
}
