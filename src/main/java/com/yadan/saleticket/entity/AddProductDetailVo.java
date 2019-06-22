package com.yadan.saleticket.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddProductDetailVo {
    private Long hallId;
    private List<AddProductTimeVo> startTimes;
    private List<AddProductPriceVo> productPrices;
    private AddFileVo priceFile;

}


