package com.yadan.saleticket.service;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.dao.hibernate.MailAddressRepository;
import com.yadan.saleticket.dao.hibernate.MemberRepository;
import com.yadan.saleticket.entity.MailAddressReqVO;
import com.yadan.saleticket.entity.MailAddressVO;
import com.yadan.saleticket.model.MailAddress;
import com.yadan.saleticket.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MailAddressService {

    @Autowired
    private MailAddressRepository mailAddressRepository;

    @Autowired
    private MemberRepository memberRepository;

    public MailAddressVO from(MailAddress persist) {
        MailAddressVO vo = new MailAddressVO();
        BeanUtils.copyNotNullProperties(persist, vo);

        Member member = persist.getMember();
        vo.setMemberId(member.getId());
        if (member.getDefaultAddress().getId().equals(persist.getId())) {
            vo.setDefaultAddress(true);
        } else {
            vo.setDefaultAddress(false);
        }
        return vo;
    }

    @Transactional
    public MailAddress add(Member member, MailAddressReqVO mailAddressReqVO) {
        MailAddress persist = new MailAddress();
        BeanUtils.copyNotNullProperties(mailAddressReqVO, persist);
        persist.setMember(member);
        mailAddressRepository.save(persist);

        if (mailAddressReqVO.getDefaultAddress()) {
            member.setDefaultAddress(persist);
            memberRepository.save(member);
        }

        return persist;
    }


    @Transactional
    public MailAddress update(Member member, MailAddressReqVO mailAddressReqVO) {
        MailAddress persist = mailAddressRepository.findByMemberAndId(member, mailAddressReqVO.getId());
        if (persist == null) {
            throw new ServiceException(ExceptionCode.INVALID_MAIL_ADDRESS, "收件地址不存在");
        }
        BeanUtils.copyNotNullProperties(mailAddressReqVO, persist);
        mailAddressRepository.save(persist);

        if (mailAddressReqVO.getDefaultAddress()) {
            member.setDefaultAddress(persist);
            memberRepository.save(member);
        }
        return persist;
    }


    @Transactional
    public Long delete(Member member, Long id) {
        MailAddress persist = mailAddressRepository.findByMemberAndId(member, id);
        if (persist == null) {
            throw new ServiceException(ExceptionCode.INVALID_MAIL_ADDRESS, "收件地址不存在");
        }

        if (member.getDefaultAddress().getId().equals(id)) {
            member.setDefaultAddress(null);
            memberRepository.save(member);
        }

        mailAddressRepository.delete(persist);
        return id;
    }


    public List<MailAddress> list(Member member, Integer page, Integer size) {
        return mailAddressRepository.findAllByMember(member, PageRequest.of(page - 1, size, Sort.Direction.DESC, "id"));
    }
}
