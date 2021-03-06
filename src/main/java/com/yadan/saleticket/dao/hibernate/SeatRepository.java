package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.theatre.Hall;
import com.yadan.saleticket.model.theatre.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface
SeatRepository extends ExtJpaRepository<Seat, Long> {
    @Query("select s from Seat s where s.hall=:hall order by s.siteRow,s.siteColumn asc ")
    List<Seat> findAllByHall(@Param("hall") Hall hall);

    List<Seat> findAllByHallAndValid(Hall hall, Boolean valid);

    List<Seat> findAllByHallOrderBySeatColumnDesc(Hall hall);
}
