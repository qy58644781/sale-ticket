package com.yadan.saleticket.model;


import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;

/**
 * 演出厅
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_Hall set is_deleted=1,update_time=now() where id=?")
public class Hall extends BaseModel {

    /**
     * 放映厅名称
     */
    private String name;

    //todo 待定，和乐脑讨论下
    /**
     * 放映厅的组织结构 String site = "5,0,0,4|11|11|11"
     */
    private String construct;

    //todo
    /**
     * 组织架构的图片
     */
    private String constructPic;

    /**
     * 剧院外键
     */
    private Long theatreId;
}
