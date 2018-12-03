package com.yadan.saleticket.model.Product;


import com.yadan.saleticket.enums.ApproveStausEnum;
import com.yadan.saleticket.model.base.BaseModel;
import com.yadan.saleticket.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.List;

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
     * 是否在线选座
     */
    private Boolean onlineSale;

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

    /**
     * 审核状态
     */
    private ApproveStausEnum approveStausEnum;

    /**
     * 创建人
     */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creater", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    private User creater;

    /**
     * 修改人
     */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    private User updater;

    /**
     * 审核人
     */
    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    private User approver;

}
