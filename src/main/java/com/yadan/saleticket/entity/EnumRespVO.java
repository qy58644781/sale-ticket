package com.yadan.saleticket.entity;

import lombok.Data;

@Data
public class EnumRespVO {
    public EnumRespVO(String code, String cn) {
        this.code = code;
        this.cn = cn;
    }

    private String code;
    private String cn;
}
