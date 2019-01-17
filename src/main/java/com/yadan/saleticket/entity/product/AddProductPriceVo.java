package com.yadan.saleticket.entity.product;

import com.yadan.saleticket.enums.TicketTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddProductPriceVo {
    private Integer inventory;
    private Integer seatFrom;
    private Integer seatTo;
    private BigDecimal price;
    private TicketTypeEnum ticketTypeEnum;
    private String areaName;
}
