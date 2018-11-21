package com.yadan.saleticket.model.Product;


import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * 商品
 */
@Audited
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_product set is_deleted=1,update_time=now() where id=?")
public class Product extends BaseModel {

    /**
     * 名称
     */
    private String name;

    /**
     * 详情
     */
    @Column(columnDefinition = "text")
    private String description;

    /**
     * 产品海报图
     */
    private String pic;

    /**
     * 产品视频
     */
    private String video;

    /**
     * 演出时长（分钟）
     */
    private Integer length;


}
