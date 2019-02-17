package com.yadan.saleticket.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.base.BaseModel;
import com.yadan.saleticket.model.theatre.Seat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * 商品价格
 */
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_product_price set is_deleted=1,update_time=now() where id=?")
public class ProductPrice extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference
    private ProductDetail productDetail;

    /**
     * 库存（非在线选座的时候是手动输入，在线选择是根据座位求和）
     */
    private Integer inventory;

    /**
     * 从第几排开始
     * 用于（非在线选座）
     */
    private Integer seatFrom;

    /**
     * 到第几排结束
     * 用于（非在线选座）
     */
    private Integer seatTo;

    /**
     * 区域名称
     * 用于（非在线选座）
     */
    private String areaName;

    /**
     * 座位
     * 用于（在线选座）
     */
    @OneToMany
    @JoinTable(name = "product_price_seat_ref",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            joinColumns = {@JoinColumn(name = "product_price_id")},
            inverseJoinColumns = {@JoinColumn(name = "seat_id")})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Seat> seats;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 票类型
     */
    @Enumerated(EnumType.STRING)
    private TicketTypeEnum ticketTypeEnum;

}
