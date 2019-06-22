package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.BuyTicketUserInfo;
import com.yadan.saleticket.model.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BuyTicketUserInfoRepository extends ExtJpaRepository<BuyTicketUserInfo, Long> {

    BuyTicketUserInfo findByMemberAndId(Member member, Long id);

    List<BuyTicketUserInfo> findAllByMember(Member member, Pageable pageable);
}
