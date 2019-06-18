package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends ExtJpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    Page<User> findAllByOrderByIdAsc(Pageable pageable);

    User findUserByMobile(String mobile);

    User findUserByMobileAndPassword(String mobile, String password);
}
