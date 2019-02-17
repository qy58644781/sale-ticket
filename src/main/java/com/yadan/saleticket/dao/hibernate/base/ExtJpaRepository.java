package com.yadan.saleticket.dao.hibernate.base;

import com.yadan.saleticket.entity.PageVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Map;

@NoRepositoryBean
public interface ExtJpaRepository<T, ID extends Long> extends JpaRepository<T, ID> {
    T merge(T t);

    PageVo<T> findAllByFilterAndPageRequest(STPageRequest stPageRequest);
}