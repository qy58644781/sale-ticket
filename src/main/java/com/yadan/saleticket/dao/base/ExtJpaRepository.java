package com.yadan.saleticket.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExtJpaRepository<T, ID extends Long> extends JpaRepository<T, ID> {
    T merge(T t);

    void remove(T t);
}