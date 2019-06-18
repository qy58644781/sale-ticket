package com.yadan.saleticket.dao.hibernate;

import com.yadan.saleticket.dao.hibernate.base.ExtJpaRepository;
import com.yadan.saleticket.model.SmsVerify;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SmsVerifyRepository extends ExtJpaRepository<SmsVerify, Long> {
    @Query(value = "select * from yd_st_sms_verify " +
            "where mobile=?1 and code=?2 " +
            "and TIMESTAMPDIFF(SECOND, create_time, now()) < ?3 " +
            "and valid = true ",
            nativeQuery = true)
    List<SmsVerify> findValid(String mobile, String code, Integer validSeconds);

    @Query(value = "select * from yd_st_sms_verify " +
            "where mobile=?1 " +
            "and TIMESTAMPDIFF(SECOND, create_time, now()) < ?2 " +
            "and valid = true ",
            nativeQuery = true)
    List<SmsVerify> findRecentCode(String mobile, Integer validSeconds);
}
