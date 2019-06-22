package com.yadan.saleticket.entity.wx;

import com.yadan.saleticket.enums.SexEnum;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WxRegisterReqVO {

    @ApiParam(value = "手机号", required = true)
    @NotNull
    private String mobile;

    @ApiParam(value = "密码", required = true)
    @NotNull
    private String password;

    @ApiParam(value = "短信验证码", required = true)
    @NotNull
    private String code;

    @ApiParam(required = true)
    @NotNull
    private String openId;

    private String nickname;

    private SexEnum sexEnum;

    private String avatarUrl;
}
