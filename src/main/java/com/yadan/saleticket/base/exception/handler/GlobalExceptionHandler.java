package com.yadan.saleticket.base.exception.handler;


import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.GlobalException;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.http.ResponseError;
import com.yadan.saleticket.base.http.STRequest;
import com.yadan.saleticket.base.http.STResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(Throwable.class)
    public STResponse handlerException(Throwable throwable) {
        log.error("系统级别捕捉异常", throwable);

        STResponse response = new STResponse<>();
        response.setResult(null);
        response.setError(getError(throwable));
        response.setRequestId(STRequest.REQUEST_ID.get());

        return response;
    }

    private ResponseError getError(Throwable throwable) {
        ResponseError error = new ResponseError();
        error.setCode(ExceptionCode.SYSTEM.getCodeNumber());

        if (throwable instanceof GlobalException) {
            error.setCode(((GlobalException) throwable).getCode().getCodeNumber());
        } else if (throwable instanceof NoHandlerFoundException) {
            error.setCode(ExceptionCode.NO_HANDLER.getCodeNumber());
        } else if (throwable instanceof HttpMessageNotReadableException) {
            error.setCode(ExceptionCode.PARAM_TYPE_ERROR.getCodeNumber());
        }

        if (!(throwable instanceof ServiceException)) {
            error.setMessage("服务器错误");
        } else {
            error.setMessage(throwable.getMessage());
        }
        return error;

    }

}
