package com.yadan.saleticket.model.order;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.enums.TicketStatusEnum;
import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.base.BaseModel;
import com.yadan.saleticket.model.product.Product;
import com.yadan.saleticket.model.product.ProductPrice;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

//@Audited
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_ticket set is_deleted=1,update_time=now() where id=?")
public class Ticket extends BaseModel {

    /**
     * 票号
     */
    private String number;

    /**
     * 票状态（待设计：维度 是否已经打印？）
     */
    @Enumerated(EnumType.STRING)
    private TicketStatusEnum ticketStatusEnum;

    /**
     * 原价
     */
    private BigDecimal originPrice;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 排
     */
    private Integer seatRow;

    /**
     * 座
     */
    private Integer seatColumn;

    /**
     * 产品价格Id
     */
//    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_price_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductPrice productPrice;

    /**
     * 所属订单Id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference
    private Order order;

}
