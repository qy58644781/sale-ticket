package com.yadan.saleticket.model;

import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_address set is_deleted=1,update_time=now() where id=?")
public class Address extends BaseModel {
    private Long provinceId;
    private Long cityId;
    private Long areaId;
    private Long userId;
    private String roadDetails;
    private String mobile;
    private String email;
}
