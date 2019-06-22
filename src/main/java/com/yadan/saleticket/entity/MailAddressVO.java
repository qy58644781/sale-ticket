package com.yadan.saleticket.entity;

import lombok.Data;

@Data
public class MailAddressVO {

    private Long id;

    private Long memberId;

    private String province;

    private String city;

    private String area;

    private String postCode;

    private String addressDetail;

    private String linkman;

    private String phone;

    private Boolean defaultAddress;

    private String tags;

}
