package com.yadan.saleticket.model.order;


import com.yadan.saleticket.enums.OrderStatusEnum;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

/**
 * 订单
 */
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
    private BigDecimal quantity;

    /**
     * 原价
     */
    private BigDecimal OriginPrice;

    /**
     * 最终价格
     */
    private BigDecimal price;

    /**
     * 订单购买人
     */
    private Long userId;
}
