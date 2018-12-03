package com.yadan.saleticket.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddProductTimeVo {
    private Integer times;
    private LocalDateTime startTime;
}
