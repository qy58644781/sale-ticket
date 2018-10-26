package com.yadan.saleticket.model;

import com.yadan.saleticket.enums.AreaLevelEnum;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

/**
 * 地区表
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_area set is_deleted=1,update_time=now() where id=?")
public class Area extends BaseModel {
    private String name;
    private String shortName;
    private Long parentId;
    private AreaLevelEnum areaLevelEnum;
    private String phoneCode;
}
