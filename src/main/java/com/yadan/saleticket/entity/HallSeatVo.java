package com.yadan.saleticket.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用于描绘一个厅的座位排列情况
 */
@Getter
@Setter
public class HallSeatVo {
    private Long theatreId;
    private Long hallId;

    private List<List<String>> seats;
}
