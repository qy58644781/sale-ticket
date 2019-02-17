package com.yadan.saleticket.entity;

import com.yadan.saleticket.enums.SexEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserVo {
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
     * 密码
     */
    private String password;

    /**
     * 用户性别
     */
    private SexEnum sexEnum;

    private AddFileVo portal;

}
