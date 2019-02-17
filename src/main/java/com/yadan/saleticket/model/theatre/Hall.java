package com.yadan.saleticket.model.theatre;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

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

    /**
     * 演出厅图片
     */
    private String constructPic;

    /**
     * 剧院
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theatre_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference
    private Theatre theatre;

    /**
     * 最大座位排，用于配合座位的row，计算出座位所在位置
     */
    private Integer maxRow;

    /**
     * 最大座位列，用于配合座位的column，计算出座位所在位置
     */
    private Integer maxColumn;
}
