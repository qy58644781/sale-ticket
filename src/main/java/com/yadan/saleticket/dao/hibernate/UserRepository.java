package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.user.User;

public interface UserRepository extends ExtJpaRepository<User, Long> {

    User findUserByMobile(String mobile);

    User findUserByMobileAndPassword(String mobile, String password);
}
