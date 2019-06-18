package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.Member;

public interface MemberRepository extends ExtJpaRepository<Member, Long> {

    Member findMemberByMobile(String mobile);

    Member findMemberByMobileAndPassword(String mobile, String password);

    Member findMemberByOpenIdAndSessionKey(String openId, String sessionKey);
}
