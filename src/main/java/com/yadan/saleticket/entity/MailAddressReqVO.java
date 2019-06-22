package com.yadan.saleticket.entity;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MailAddressReqVO {

    private Long id;

    @ApiParam(value = "省", required = true)
    @NotNull
    private String province;

    @ApiParam(value = "市", required = true)
    @NotNull
    private String city;

    @ApiParam(value = "区", required = true)
    @NotNull
    private String area;

    @ApiParam(value = "邮编", required = true)
    @NotNull
    private String postCode;

    @ApiParam(value = "详细地址", required = true)
    @NotNull
    private String addressDetail;

    @ApiParam(value = "联系人", required = true)
    @NotNull
    private String linkman;

    @ApiParam(value = "联系电话", required = true)
    @NotNull
    private String phone;

    @ApiParam(value = "是否默认地址", required = true)
    @NotNull
    private Boolean defaultAddress;

    @ApiParam(value = "标签")
    private String tags;
}
