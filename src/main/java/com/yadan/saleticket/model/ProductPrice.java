package com.yadan.saleticket.model;

import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;


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

    /**
     * 演出场次
     */
    private Integer times;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 上映时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 第几排开始
     */
    private Integer rowFrom;

    /**
     * 第几排结束
     */
    private Integer rowEnd;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 演出厅id
     */
    private Long hallId;

    /**
     * 所属产品Id
     */
    private Long productId;

}
