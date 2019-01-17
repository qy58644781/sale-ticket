package com.yadan.saleticket.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageVo<T> {
    private List<T> content;
    private Long totalElements;
}
