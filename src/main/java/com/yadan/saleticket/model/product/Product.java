package com.yadan.saleticket.model.product;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.enums.ApproveStatusEnum;
import com.yadan.saleticket.model.base.BaseModel;
import com.yadan.saleticket.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_product set is_deleted=1,update_time=now() where id=?")
public class Product extends BaseModel {

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

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
    @Enumerated(EnumType.STRING)
    private ApproveStatusEnum approveStatusEnum;

    /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creater", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Where(clause = "is_deleted=0")
    private User creater;

    /**
     * 修改人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Where(clause = "is_deleted=0")
    private User updater;

    /**
     * 审核人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Where(clause = "is_deleted=0")
    private User approver;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Where(clause = "is_deleted=0")
    private List<ProductDetail> productDetails;

}
