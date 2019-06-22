package com.yadan.saleticket.service;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.dao.hibernate.BuyTicketUserInfoRepository;
import com.yadan.saleticket.entity.BuyTicketUserInfoReqVO;
import com.yadan.saleticket.entity.BuyTicketUserInfoVO;
import com.yadan.saleticket.model.BuyTicketUserInfo;
import com.yadan.saleticket.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyTicketUserInfoService {

    @Autowired
    private BuyTicketUserInfoRepository buyTicketUserInfoRepository;


    public BuyTicketUserInfoVO from(BuyTicketUserInfo persist) {
        BuyTicketUserInfoVO vo = new BuyTicketUserInfoVO();
        BeanUtils.copyNotNullProperties(persist, vo);
        vo.setMemberId(persist.getMember().getId());
        return vo;
    }

    /**
     * 添加购票人信息
     *
     * @return
     */
    public BuyTicketUserInfo add(Member member, BuyTicketUserInfoReqVO buyTicketUserInfoReqVO) {
        BuyTicketUserInfo persist = new BuyTicketUserInfo();
        BeanUtils.copyNotNullProperties(buyTicketUserInfoReqVO, persist);
        persist.setMember(member);
        buyTicketUserInfoRepository.save(persist);
        return persist;
    }

    /**
     * 更新购票人信息
     *
     * @return
     */
    public BuyTicketUserInfo update(Member member, BuyTicketUserInfoReqVO buyTicketUserInfoReqVO) {
        BuyTicketUserInfo persist = buyTicketUserInfoRepository.findByMemberAndId(member, buyTicketUserInfoReqVO.getId());
        if (persist == null) {
            throw new ServiceException(ExceptionCode.INVALID_BUY_TICKET_USER_INFO, "购票人不存在");
        }
        BeanUtils.copyNotNullProperties(buyTicketUserInfoReqVO, persist);
        buyTicketUserInfoRepository.save(persist);
        return persist;
    }

    /**
     * 删除购票人信息
     *
     * @return
     */
    public Long delete(Member member, Long infoId) {
        BuyTicketUserInfo persist = buyTicketUserInfoRepository.findByMemberAndId(member, infoId);
        if (persist == null) {
            throw new ServiceException(ExceptionCode.INVALID_BUY_TICKET_USER_INFO, "购票人不存在");
        }
        buyTicketUserInfoRepository.delete(persist);
        return infoId;
    }

    /**
     * 获取所有购票人信息
     *
     * @param member
     * @return
     */
    public List<BuyTicketUserInfo> list(Member member, Integer page, Integer size) {
        return buyTicketUserInfoRepository.findAllByMember(member, PageRequest.of(page - 1, size, Sort.Direction.DESC, "id"));
    }
}
