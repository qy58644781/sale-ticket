package com.yadan.saleticket.enums;

public enum SexEnum {
    MALE("男"), FEMALE("女"), UNKNOW("未知");

    private String cn;

    SexEnum(String val) {
        this.cn = val;
    }

    public String getCn() {
        return cn;
    }

    public void setVal(String val) {
        this.cn = val;
    }
}
