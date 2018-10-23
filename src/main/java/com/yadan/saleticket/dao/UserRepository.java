package com.yadan.saleticket.dao;

import com.yadan.saleticket.dao.base.ExtJpaRepository;
import com.yadan.saleticket.model.User;

public interface UserRepository extends ExtJpaRepository<User, Long> {

    User findUserByMobile(String mobile);
}
