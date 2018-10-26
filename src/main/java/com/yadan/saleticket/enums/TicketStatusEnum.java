package com.yadan.saleticket.enums;

public enum TicketStatusEnum {
    /**
     * 有效的票
     */
    VALID,

    /**
     * 已经退了的票
     */
    REFUND,

    /**
     * 已售出
     */
    SAILED,

    /**
     * 无效 （座位损坏被下架，该座位不进行售卖等）
     */
    INVALID


}
