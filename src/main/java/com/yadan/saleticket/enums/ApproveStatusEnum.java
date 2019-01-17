package com.yadan.saleticket.enums;

public enum ApproveStatusEnum {
    TBD("待审核"),
    PASSED("审核通过"),
    UNPASSED("审核失败");

    private String val;

    ApproveStatusEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public static boolean pass(ApproveStatusEnum approveStatusEnum) {
        if (approveStatusEnum == PASSED)
            return true;
        else
            return false;
    }
}
