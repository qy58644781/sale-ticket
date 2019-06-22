package com.yadan.saleticket.entity.wx;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WxPhoneReqVO {

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
