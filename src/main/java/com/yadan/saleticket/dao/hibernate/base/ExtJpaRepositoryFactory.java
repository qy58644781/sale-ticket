package com.yadan.saleticket.dao.hibernate.base;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

public class ExtJpaRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
    private final EntityManager em;

    public ExtJpaRepositoryFactory(EntityManager em) {
        super(em);
        this.em = em;
    }


    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
            return QuerydslJpaPredicateExecutor.class;
        } else {
            return SimpleExtJpaRepository.class;
        }
    }

    private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
        return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }
}
