package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.order.Order;

public interface OrderRepository extends ExtJpaRepository<Order, Long> {
}
