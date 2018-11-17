package com.yadan.saleticket.dao.hibernate.base;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;
import java.io.Serializable;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

public class ExtJpaRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
    private final EntityManager em;

    public ExtJpaRepositoryFactory(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    protected <T, ID extends Serializable> SimpleExtJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType());
        return getTargetRepositoryViaReflection(information, entityInformation, entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
            return QueryDslJpaRepository.class;
        } else {
            return SimpleExtJpaRepository.class;
        }
    }

    private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
        return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
    }
}
