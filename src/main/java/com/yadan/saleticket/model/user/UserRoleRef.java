package com.yadan.saleticket.model.user;


import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

/**
 * 用户权限关系表
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_user_role_ref set is_deleted=1,update_time=now() where id=?")
public class UserRoleRef extends BaseModel {
    private Long userId;
    private Long roleId;
}
