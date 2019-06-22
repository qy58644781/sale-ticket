package com.yadan.saleticket.entity;

import com.yadan.saleticket.enums.CredentialTypeEnum;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IdAuthenReqVO {

    @ApiParam(value = "真实姓名", required = true)
    @NotNull
    private String realname;

    @ApiParam(value = "证件类型", required = true)
    @NotNull
    private CredentialTypeEnum credentialTypeEnum;

    @ApiParam(value = "证件号", required = true)
    @NotNull
    private String credentialNo;
}
