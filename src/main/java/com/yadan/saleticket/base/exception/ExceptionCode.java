package com.yadan.saleticket.base.exception;

public enum ExceptionCode {
    PARAM_TYPE_ERROR(1000),
    NOT_FOUND(1001),
    NO_PERMISSION(1002),
    SYSTEM(1003),
    SERVICE(1004),
    NO_HANDLER(1005),
    JSON_CONVERT_EXCEPTION(1006),

    ;

    private int codeNumber;

    ExceptionCode(int codeNumber) {
        this.codeNumber = codeNumber;
    }

    public int getCodeNumber() {
        return codeNumber;
    }
}
