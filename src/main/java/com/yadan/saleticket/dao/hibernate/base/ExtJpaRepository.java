package com.yadan.saleticket.dao.hibernate.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;

@NoRepositoryBean
public interface ExtJpaRepository<T, ID extends Long> extends JpaRepository<T, ID> {

    EntityManager getEntityManager();
}