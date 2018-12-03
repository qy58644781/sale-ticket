package com.yadan.saleticket.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddProductVo {
    /**
     * 话剧名称
     */
    private String name;

    /**
     * 是否在线售票
     */
    private Boolean onlineSale = false;

    /**
     * 是否使用excel导入
     */
    private Boolean useExcel = false;

    /**
     * 话剧时长
     */
    private Integer length;

    private List<AddProductDetailVo> productDetails;

}
