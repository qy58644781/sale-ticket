package com.yadan.saleticket.entity;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WxRegisterReqVO {

    @ApiParam(value = "手机号", required = true)
    @NotNull
    private String mobile;

    @ApiParam(value = "短信验证码", required = true)
    @NotNull
    private String code;

    @ApiParam(required = true)
    @NotNull
    private String openId;

    @ApiParam(required = true)
    @NotNull
    private String encryptedData;

    @ApiParam(required = true)
    @NotNull
    private String iv;
}
