package com.yadan.saleticket.dao.hibernate.base;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Map;

@NoRepositoryBean
public interface ExtJpaRepository<T, ID extends Long> extends JpaRepository<T, ID> {
    T merge(T t);

    Page findAllByFilterAndPageRequest(STPageRequest stPageRequest, Map<String, String> filter, Class<T> clazz);
}