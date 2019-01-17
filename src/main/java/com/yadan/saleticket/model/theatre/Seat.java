package com.yadan.saleticket.model.theatre;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_Seat set is_deleted=1,update_time=now() where id=?")
public class Seat extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Hall hall;

    /**
     * 所属区域名称
     * 如：一楼、二楼、侧面
     */
    private String areaName;

    /**
     * 对应x轴
     */
    private Integer seatRow;

    /**
     * 对应y轴
     */
    private Integer seatColumn;

    /**
     * 是否可用
     */
    private Boolean valid;

}
