package com.yadan.saleticket.entity;

import com.yadan.saleticket.enums.SexEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class AppMemberVO {
    private Long id;
    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户手机/账户
     */
    private String mobile;
    /**
     * 用户性别
     */
    @Enumerated(EnumType.STRING)
    private SexEnum sexEnum;

    private String openId;

    /**
     * 省
     */
    private String city;

    /**
     * 市
     */
    private String province;

    /**
     * 区
     */
    private String country;

    /**
     * 头像url
     */
    private String avatarUrl;
}
