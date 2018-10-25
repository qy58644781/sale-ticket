package com.yadan.saleticket.model;


import com.yadan.saleticket.enums.TicketStatus;
import com.yadan.saleticket.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Audited
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
     * 票状态
     */
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    /**
     * 价格
     */
    private BigDecimal price;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
