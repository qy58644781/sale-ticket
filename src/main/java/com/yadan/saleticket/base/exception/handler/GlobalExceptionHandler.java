package com.yadan.saleticket.base.exception.handler;


import com.google.common.base.Joiner;
import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.GlobalException;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.http.ResponseError;
import com.yadan.saleticket.base.http.STRequest;
import com.yadan.saleticket.base.http.STResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;


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
            error.setMessage(throwable.getMessage());
        } else if (throwable instanceof NoHandlerFoundException) {
            error.setCode(ExceptionCode.NO_HANDLER.getCodeNumber());
            error.setMessage("服务器错误");
        } else if (throwable instanceof HttpMessageNotReadableException) {
            error.setCode(ExceptionCode.PARAM_TYPE_ERROR.getCodeNumber());
            error.setMessage("服务器错误");
        } else if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exs = (MethodArgumentNotValidException) throwable;
            List<ObjectError> errList = exs.getBindingResult().getAllErrors();
            List<String> errMsgs = errList.stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
            error.setMessage(String.format("参数校验失败: %s", Joiner.on(", ").skipNulls().join(errMsgs)));
            error.setCode(ExceptionCode.REQUEST_PARAM_ERROR.getCodeNumber());
        }

        return error;

    }

}
