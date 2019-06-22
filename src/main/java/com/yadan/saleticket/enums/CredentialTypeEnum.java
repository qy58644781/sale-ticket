package com.yadan.saleticket.enums;

/**
 * 证件类型
 */
public enum CredentialTypeEnum {

    /**
     * 身份证
     */
    ID_CARD("身份证"),

    PASSPORT("护照"),

    CERTIFICATE_OF_OFFICERS("军官证")
    ;

    private String cn;
    CredentialTypeEnum(String val) {
        this.cn = val;
    }

    public String getCn() {
        return cn;
    }

    public void setVal(String val) {
        this.cn = val;
    }
}
