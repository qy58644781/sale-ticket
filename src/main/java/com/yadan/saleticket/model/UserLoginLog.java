package com.yadan.saleticket.model;

import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;


// todo
/**
 * 用户登录日志
 *    用户记录用户的登录token，作为单点登录，防止2台手机可以登录同一个账户
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_UserLoginLog set is_deleted=1,update_time=now() where id=?")
public class UserLoginLog extends BaseModel {
    /**
     * 用户外键
     */
    private Long userId;

    /**
     * 登录秘钥
     */
    private String headerSecurityToken;
}
