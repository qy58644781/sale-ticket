package com.yadan.saleticket.model.Product;

import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.Theatre.Seat;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 商品价格
 */
@Audited
@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_product set is_deleted=1,update_time=now() where id=?")
public class ProductPrice extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
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
    @NotAudited
    @OneToMany
    @JoinTable(name = "product_price_seat_ref",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            joinColumns = {@JoinColumn(name = "product_price_id")},
            inverseJoinColumns = {@JoinColumn(name = "seat_id")})
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
