package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.order.Order;
import com.yadan.saleticket.model.order.Ticket;

public interface TicketRepository extends ExtJpaRepository<Ticket, Long> {
}
