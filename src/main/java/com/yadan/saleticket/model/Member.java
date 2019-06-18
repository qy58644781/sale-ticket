package com.yadan.saleticket.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_user set is_deleted=1,update_time=now() where id=?")
public class Member extends User {

    private String openId;

    private String sessionKey;

    private String unionId;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;
}
