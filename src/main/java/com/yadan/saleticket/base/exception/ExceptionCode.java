package com.yadan.saleticket.base.exception;

public enum ExceptionCode {
    // 1XXX 系统错误
    PARAM_TYPE_ERROR(1000),
    NOT_FOUND(1001),
    NO_PERMISSION(1002),
    SYSTEM(1003),
    SERVICE(1004),
    NO_HANDLER(1005),
    JSON_CONVERT_EXCEPTION(1006),

    // 2XXX 业务错误
    MOBILEFORMATERROR(2001),
    INVALID_CODE(2002),
    INVALID_USER(2003),
    INVALID_EXCEL_DATA(2004),
    INVALID_ADD_PRODUCT_VO(2005),
    INVALID_SEAT(2006),
    INVALID_PRODUCT(2007),
    INVALID_CREATE_ORDER(2008),
    ;
    private int codeNumber;

    ExceptionCode(int codeNumber) {
        this.codeNumber = codeNumber;
    }

    public int getCodeNumber() {
        return codeNumber;
    }
}
