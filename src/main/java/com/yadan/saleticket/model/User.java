package com.yadan.saleticket.model;

import com.yadan.saleticket.enums.Sex;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_user set is_deleted=1,update_time=now() where id=?")
public class User extends BaseModel {

    private String nickname;

    private String mobile;

    @Enumerated(EnumType.STRING)
    private Sex sex;
}
