package com.yadan.saleticket.dao.redis;

@FunctionalInterface
public interface LockBusiness {
    Object doBusiness();
}
