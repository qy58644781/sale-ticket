package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.Theatre.Seat;

public interface SeatRepository extends ExtJpaRepository<Seat, Long> {
}
