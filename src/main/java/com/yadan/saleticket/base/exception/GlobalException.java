package com.yadan.saleticket.base.exception;

public class GlobalException extends RuntimeException {
    private ExceptionCode code;
    private String message;

    public GlobalException(ExceptionCode code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ExceptionCode getCode() {
        return code;
    }

    public void setCode(ExceptionCode code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
