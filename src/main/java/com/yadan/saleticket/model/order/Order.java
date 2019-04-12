package com.yadan.saleticket.model.order;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yadan.saleticket.enums.OrderStatusEnum;
import com.yadan.saleticket.model.base.BaseModel;
import com.yadan.saleticket.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单
 */
//@Audited
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_order set is_deleted=1,update_time=now() where id=?")
public class Order extends BaseModel {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatusEnum;

    /**
     * 购票数量
     */
    private Integer quantity;

    /**
     * 原价
     */
    private BigDecimal OriginPrice;

    /**
     * 最终价格
     */
    private BigDecimal price;

    /**
     * 订单所属用户
     */
//    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    private User user;

    /**
     * 下单人
     */
//    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creater", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    private User creater;

    @OneToMany(mappedBy = "order")
    @org.hibernate.annotations.ForeignKey(name = "none")
//    @Where(clause = "is_deleted=0")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @JsonManagedReference
    private List<Ticket> tickets;
}
