package com.yadan.saleticket.entity;

import com.yadan.saleticket.enums.CredentialTypeEnum;
import lombok.Data;

@Data
public class BuyTicketUserInfoVO {

    private Long id;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 证件类型
     */
    private CredentialTypeEnum credentialTypeEnum;

    /**
     * 证件号码
     */
    private String credentialNo;

    private Long memberId;
}
