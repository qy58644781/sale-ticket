package com.yadan.saleticket.enums;

public enum ApproveStausEnum {
    TBD("待审核"),
    PASSED("审核通过"),
    UNPASSED("审核失败");

    private String val;
    ApproveStausEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
