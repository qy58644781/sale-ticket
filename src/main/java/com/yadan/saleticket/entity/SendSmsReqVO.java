package com.yadan.saleticket.entity;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SendSmsReqVO {

    @ApiParam(value = "手机号", required = true)
    @NotNull
    private String mobile;
}
