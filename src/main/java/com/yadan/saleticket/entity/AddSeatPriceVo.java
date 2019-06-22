package com.yadan.saleticket.entity;

import com.yadan.saleticket.enums.TicketTypeEnum;
import com.yadan.saleticket.model.theatre.Seat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddSeatPriceVo {
    private Seat seat;
    private BigDecimal price;
    private TicketTypeEnum ticketTypeEnum;
}
