package com.yadan.saleticket.base.exception;

public class SystemException extends GlobalException {

    public SystemException() {
        super(ExceptionCode.SYSTEM, "服务器错误");
    }

    public SystemException(ExceptionCode code, String message) {
        super(code, message);
    }
}
