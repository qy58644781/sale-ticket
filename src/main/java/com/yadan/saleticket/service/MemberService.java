package com.yadan.saleticket.service;

import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.base.tools.MD5;
import com.yadan.saleticket.dao.hibernate.MemberRepository;
import com.yadan.saleticket.entity.app.AppMemberVO;
import com.yadan.saleticket.enums.CredentialTypeEnum;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.Member;
import com.yadan.saleticket.model.User;
import com.yadan.saleticket.model.WxMaJscode2Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends AbstractUserService {

    @Autowired
    private MemberRepository memberRepository;

    public AppMemberVO from(Member member) {
        AppMemberVO vo = new AppMemberVO();
        BeanUtils.copyNotNullProperties(member, vo);
        return vo;
    }

    public Member fromUser(User user) {
        return memberRepository.findMemberByMobile(user.getMobile());
    }


    /**
     * 注册用户
     */
    public Member register(String mobile, String password) {
        Member member = new Member();
        member.setMobile(mobile);
        member.setSexEnum(SexEnum.UNKNOW);
        member.setPassword(MD5.MD5Encode(password));
        member.setNickname(mobile);
        memberRepository.save(member);
        return member;
    }

    /**
     * 注册用户
     */
    public Member register(String mobile, String password, String nickname,
                           SexEnum sexEnum, String avatarUrl, WxMaJscode2Session session) {
        Member member = new Member();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setSexEnum(sexEnum);
        member.setPassword(MD5.MD5Encode(password));
        member.setOpenId(session.getOpenId());
        member.setUnionId(session.getUnionId());
        member.setAvatarUrl(avatarUrl);
        memberRepository.save(member);
        return member;
    }

    /**
     * 实名认证
     * @return
     */
    public Member identityAuthentication(Member member, String realname, CredentialTypeEnum credentialTypeEnum, String credentialNo) {
        member.setRealname(realname);
        member.setCredentialTypeEnum(credentialTypeEnum);
        member.setCredentialNo(credentialNo);
        memberRepository.save(member);
        return member;
    }



}
