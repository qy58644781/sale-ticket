package com.yadan.saleticket.model.user;

import com.yadan.saleticket.base.security.HeaderSecurityTokenEnum;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;


/**
 * 用户登录日志
 * 用户记录用户的登录token，作为单点登录，防止多端可以登录同一个账户
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_User_Login_Token_Log set is_deleted=1,update_time=now() where id=?")
public class UserLoginTokenLog extends BaseModel {
    /**
     * 用户外键
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    /**
     * 登录秘钥
     */
    private String headerSecurityToken;

    /**
     * 区分前后端登录
     */
    @Enumerated(EnumType.STRING)
    private HeaderSecurityTokenEnum tokenType;

}
