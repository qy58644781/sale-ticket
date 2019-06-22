package com.yadan.saleticket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.enums.AreaLevelEnum;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 地区表
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_area set is_deleted=1,update_time=now() where id=?")
public class Area extends BaseModel {

    private String region_name;

    private String shortName;

    private String pinyinName;

    private String pinyinShortName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Area parent;

    @Enumerated(EnumType.STRING)
    private AreaLevelEnum areaLevelEnum;

    private String phoneCode;

    /**
     * 邮编
     */
    private String postCode;
}
