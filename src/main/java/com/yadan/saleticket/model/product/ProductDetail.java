package com.yadan.saleticket.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yadan.saleticket.model.base.BaseModel;
import com.yadan.saleticket.model.theatre.Hall;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Where(clause = "is_deleted=0")
@SQLDelete(sql = "update yd_st_product_detail set is_deleted=1,update_time=now() where id=?")
public class ProductDetail extends BaseModel {

    /**
     * 演出场次
     */
    private Integer times;

    /**
     * 所属产品Id
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "is_deleted=0")
    private Product product;

    /**
     * 所属放映厅
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", foreignKey = @ForeignKey(name = "", value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Where(clause = "is_deleted=0")
    private Hall hall;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "productDetail")
    @Where(clause = "is_deleted=0")
    private List<ProductPrice> productPrices;

}
