package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.base.security.HeaderSecurityTokenEnum;
import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.User;
import com.yadan.saleticket.model.UserLoginTokenLog;

import java.util.List;

public interface UserLoginTokenLogRepository extends ExtJpaRepository<UserLoginTokenLog, Long> {
    List<UserLoginTokenLog> findAllByUserAndTokenType(User user, HeaderSecurityTokenEnum tokenType);
}
