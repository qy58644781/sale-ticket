package com.yadan.saleticket.controller.wxma;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.security.STRememberMeService;
import com.yadan.saleticket.base.tools.MD5;
import com.yadan.saleticket.dao.hibernate.MemberRepository;
import com.yadan.saleticket.dao.hibernate.WxMaJscode2SessionRepository;
import com.yadan.saleticket.entity.SendSmsReqVO;
import com.yadan.saleticket.entity.app.AppLoginByPsdReqVO;
import com.yadan.saleticket.entity.app.AppLoginBySmsReqVO;
import com.yadan.saleticket.entity.wx.*;
import com.yadan.saleticket.model.Member;
import com.yadan.saleticket.model.WxMaJscode2Session;
import com.yadan.saleticket.service.MemberService;
import com.yadan.saleticket.service.SmsVerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Api(description = "wx登录注册模块")
@RestController
@RequestMapping("/wx/auth/open")
public class WxAuthController {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private WxMaJscode2SessionRepository wxMaJscode2SessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private STRememberMeService stRememberMeService;

    @Autowired
    private SmsVerifyService smsVerifyService;

    @ApiOperation("微信通过code，获取openId")
    @PostMapping("/auth")
    public String auth(@Valid @RequestBody WxAuthReqVO wxAuthReqVO) {
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(wxAuthReqVO.getCode());
            WxMaJscode2Session existedSession = wxMaJscode2SessionRepository.findWxMaJscode2SessionByOpenId(session.getOpenid());
            if (existedSession == null) {
                WxMaJscode2Session newSession = new WxMaJscode2Session();
                newSession.setOpenId(session.getOpenid());
                newSession.setSessionKey(session.getSessionKey());
                newSession.setUnionId(session.getUnionid());
                wxMaJscode2SessionRepository.save(newSession);
            } else {
                // 刷新sessionKey
                existedSession.setSessionKey(session.getSessionKey());
                wxMaJscode2SessionRepository.save(existedSession);
            }
            return session.getOpenid();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(ExceptionCode.WX_LOGIN_ERROR, e.getMessage());
        }
    }

    @ApiOperation("获取用户绑定手机号信息")
    @PostMapping("/phone")
    public WxMaPhoneNumberInfo phone(@Valid @RequestBody WxPhoneReqVO wxPhoneReqVO) {
        WxMaJscode2Session exists = wxMaJscode2SessionRepository.findWxMaJscode2SessionByOpenId(wxPhoneReqVO.getOpenId());
        if (exists == null) {
            throw new ServiceException(ExceptionCode.WX_LOGIN_ERROR, "wxSession获取失败，请重新授权");
        }
        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(exists.getSessionKey(), wxPhoneReqVO.getEncryptedData(), wxPhoneReqVO.getIv());
        return phoneNoInfo;
    }

    @ApiOperation("发送短信验证码")
    @PostMapping("/sendSms")
    public String sendSms(@Valid @RequestBody SendSmsReqVO sendSmsReqVO) {
        String code = smsVerifyService.sendSmsVerify(sendSmsReqVO.getMobile());
        return code;
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public WxMemberVO register(@ApiIgnore HttpServletRequest request,
                               @ApiIgnore HttpServletResponse response,
                               @Valid @RequestBody WxRegisterReqVO wxRegisterReqVO) {
        // 验证验证码
        smsVerifyService.validSmsVerify(wxRegisterReqVO.getMobile(), wxRegisterReqVO.getCode());

        WxMaJscode2Session session = wxMaJscode2SessionRepository.findWxMaJscode2SessionByOpenId(wxRegisterReqVO.getOpenId());
        if (session == null) {
            throw new ServiceException(ExceptionCode.WX_LOGIN_ERROR, "wxSession获取失败，请重新授权");
        }

        Member member = memberRepository.findMemberByMobile(wxRegisterReqVO.getMobile());
        if (member != null) {
            throw new ServiceException(ExceptionCode.INVALID_USER, "该手机已经被注册");
        }

        member = memberService.register(wxRegisterReqVO.getMobile(), wxRegisterReqVO.getPassword(), wxRegisterReqVO.getNickname(), wxRegisterReqVO.getSexEnum(), wxRegisterReqVO.getAvatarUrl(), session);
        stRememberMeService.createToken(request, response, member, -1);
        return new WxMemberVO(memberService.from(member), false);
    }


    @ApiOperation("账号密码登录")
    @PostMapping("/loginByPassword")
    public WxMemberVO loginByPassword(@ApiIgnore HttpServletRequest request,
                                      @ApiIgnore HttpServletResponse response,
                                      @Valid @RequestBody AppLoginByPsdReqVO appLoginByPsdReqVO) {
        Member member = memberRepository.findMemberByMobile(appLoginByPsdReqVO.getMobile());
        if (member == null) {
            member = new Member();
            member.setMobile(appLoginByPsdReqVO.getMobile());
            return new WxMemberVO(memberService.from(member), true);
        }
        member = memberRepository.findMemberByMobileAndPassword(appLoginByPsdReqVO.getMobile(), MD5.MD5Encode(appLoginByPsdReqVO.getPassword()));
        if (member == null) {
            throw new ServiceException(ExceptionCode.INVALID_USER, "用户密码错误");
        }
        stRememberMeService.createToken(request, response, member, -1);
        return new WxMemberVO(memberService.from(member), false);
    }

    @ApiOperation("短信登录")
    @PostMapping("/loginBySms")
    public WxMemberVO loginBySms(@ApiIgnore HttpServletRequest request,
                                 @ApiIgnore HttpServletResponse response,
                                 @Valid @RequestBody AppLoginBySmsReqVO appLoginBySmsReqVO) {
        smsVerifyService.validSmsVerify(appLoginBySmsReqVO.getMobile(), appLoginBySmsReqVO.getCode());

        Member member = memberRepository.findMemberByMobile(appLoginBySmsReqVO.getMobile());
        if (member == null) {
            member = new Member();
            member.setMobile(appLoginBySmsReqVO.getMobile());
            return new WxMemberVO(memberService.from(member), true);
        }

        member = memberRepository.findMemberByMobile(appLoginBySmsReqVO.getMobile());
        if (member == null) {
            throw new ServiceException(ExceptionCode.INVALID_USER, "用户手机号不存在");
        }
        stRememberMeService.createToken(request, response, member, -1);

        return new WxMemberVO(memberService.from(member), false);
    }

    @ApiOperation("微信登录")
    @PostMapping("/loginByWx")
    public WxMemberVO loginByWx(@ApiIgnore HttpServletRequest request,
                                @ApiIgnore HttpServletResponse response,
                                @Valid @RequestBody WxLoginByWxReqVO wxLoginByWxReqVO) {
        WxMaJscode2Session session = wxMaJscode2SessionRepository.findWxMaJscode2SessionByOpenId(wxLoginByWxReqVO.getOpenId());
        if (session == null) {
            throw new ServiceException(ExceptionCode.WX_LOGIN_ERROR, "wxSession获取失败，请重新授权");
        }
        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(session.getSessionKey(), wxLoginByWxReqVO.getEncryptedData(), wxLoginByWxReqVO.getIv());
        Member member = memberRepository.findMemberByMobile(phoneNoInfo.getPurePhoneNumber());
        if (member == null) {
            member = new Member();
            member.setMobile(phoneNoInfo.getPurePhoneNumber());
            return new WxMemberVO(memberService.from(member), true);
        }
        stRememberMeService.createToken(request, response, member, -1);

        return new WxMemberVO(memberService.from(member), false);
    }

}
