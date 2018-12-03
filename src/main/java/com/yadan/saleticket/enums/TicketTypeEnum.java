package com.yadan.saleticket.enums;

public enum TicketTypeEnum {
    // 正常
    NORMAL("普通"),
    // 赠送
    GIFT("赠品"),
    // 员工票
    INTERNAL("员工");

    String val;

    TicketTypeEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public static TicketTypeEnum get(String val) {
        for (TicketTypeEnum type : TicketTypeEnum.values()) {
            if (type.getVal().equals(val)) {
                return type;
            }
        }
        return null;
    }
}
