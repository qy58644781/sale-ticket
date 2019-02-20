package com.yadan.saleticket.dao.redis;

public enum RedisKeyPrefix {
    LOCK("lock-"),

    PRODUCT_DETAIL_KEY("product-detail-key");

    RedisKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String prefix;

    public String getPrefix() {
        return prefix;
    }
}
