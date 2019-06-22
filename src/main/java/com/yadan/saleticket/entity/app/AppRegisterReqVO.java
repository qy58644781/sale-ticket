package com.yadan.saleticket.entity.app;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppRegisterReqVO {

    @ApiParam(value = "手机号", required = true)
    @NotNull
    private String mobile;

    @ApiParam(value = "短信验证码", required = true)
    @NotNull
    private String code;

    @ApiParam(value = "密码", required = true)
    @NotNull
    private String password;
}
