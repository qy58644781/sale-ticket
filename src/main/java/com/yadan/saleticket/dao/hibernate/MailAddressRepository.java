package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.MailAddress;
import com.yadan.saleticket.model.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MailAddressRepository extends ExtJpaRepository<MailAddress, Long> {

    MailAddress findByMemberAndId(Member member, Long id);

    List<MailAddress> findAllByMember(Member member, Pageable pageable);
}
