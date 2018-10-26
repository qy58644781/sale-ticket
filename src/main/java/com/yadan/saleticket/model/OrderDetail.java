package com.yadan.saleticket.model;

import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_OrderDetail set is_deleted=1,update_time=now() where id=?")
public class OrderDetail extends BaseModel {

    /**
     * 原价
     */
    private BigDecimal originPrice;

    /**
     * 现价
     */
    private BigDecimal price;

    /**
     * 订单外键
     */
    private Long orderId;

    /**
     * 票外键
     */
    private Long ticketId;


}
