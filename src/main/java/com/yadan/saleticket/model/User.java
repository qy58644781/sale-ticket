package com.yadan.saleticket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.base.BaseModel;
import flexjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_user set is_deleted=1,update_time=now() where id=?")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseModel {

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
    @JsonIgnore
    @JSON(include = false)
    private String password;

    /**
     * 用户性别
     */
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private SexEnum sexEnum;

}
