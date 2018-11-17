package com.yadan.saleticket.model.Order;

import com.yadan.saleticket.enums.PayStatusEnum;
import com.yadan.saleticket.enums.PaymentPlatformEnum;
import com.yadan.saleticket.enums.PaymentTypeEnum;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_Payment set is_deleted=1,update_time=now() where id=?")
public class Payment extends BaseModel {
    private Long userId;

    private Long orderId;

    /**
     * 支付金额
     */
    @Column(scale = 2)
    private BigDecimal amount;

    /**
     * 开始支付时间
     */
    @Column
    private LocalDateTime startTime;

    /**
     * 支付成功通知时间
     */
    @Column
    private LocalDateTime endTime;

    /**
     * 支付类型：支付、退款
     */
    private PaymentTypeEnum paymentTypeEnum;

    /**
     * 支付平台
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentPlatformEnum paymentPlatformEnum;

    /**
     * 交易号
     */
    @Column(length = 100)
    private String tradeNo;

    /**
     * 第三方平台订单号
     */
    private String channelTradeNo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PayStatusEnum payStatusEnum;

    /**
     * 退款的时候，上一笔支付id
     */
    private Long parentId;
}
