package com.yadan.saleticket.entity;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WxLoginReqVO {
    @ApiParam(value = "小程序请求授权code", required = true)
    @NotNull
    private String code;
}
