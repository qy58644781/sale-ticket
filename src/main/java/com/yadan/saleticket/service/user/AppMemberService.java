package com.yadan.saleticket.service.user;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.base.tools.MD5;
import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.entity.AppMemberVO;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.Member;
import com.yadan.saleticket.model.WxMaJscode2Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppMemberService extends AbstractUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取用户信息
     */
    public Member register(String mobile, String password) {
        Member member = new Member();
        member.setMobile(mobile);
        member.setSexEnum(SexEnum.UNKNOW);
        member.setPassword(MD5.MD5Encode(password));
        member.setNickname(mobile);
        userRepository.save(member);
        return member;
    }

    public Member register(String mobile, WxMaUserInfo userInfo, WxMaJscode2Session session) {
        Member member = new Member();
        member.setMobile(mobile);
        member.setPassword(MD5.MD5Encode("123456"));
        BeanUtils.copyNotNullProperties(userInfo, member);
        BeanUtils.copyNotNullProperties(session, member);
        return member;
    }

    public AppMemberVO from(Member member) {
        AppMemberVO vo = new AppMemberVO();
        BeanUtils.copyNotNullProperties(member, vo);
        return vo;
    }
}
