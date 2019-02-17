package com.yadan.saleticket.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddHallVo {
    private Long id;
    private Long theatreId;
    private String name;
    private AddFileVo seatFile;
}
