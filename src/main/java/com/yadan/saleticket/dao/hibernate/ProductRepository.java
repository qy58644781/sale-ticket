package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.product.Product;
import com.yadan.saleticket.model.theatre.Hall;
import com.yadan.saleticket.model.theatre.Seat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends ExtJpaRepository<Product, Long> {
    @Query(value = "select count(distinct t1.id) as cnt\n" +
            "from yd_st_product t1\n" +
            "inner join yd_st_product_detail t2\n" +
            "    on t1.id = t2.product_id\n" +
            "where substr(t2.start_time, 1, 10)=:startDate", nativeQuery = true)
    Integer getCount(@Param("startDate")String startDate);
}
