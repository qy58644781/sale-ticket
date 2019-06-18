package com.yadan.saleticket.controller.wxma;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.security.STRememberMeService;
import com.yadan.saleticket.dao.hibernate.MemberRepository;
import com.yadan.saleticket.dao.hibernate.WxMaJscode2SessionRepository;
import com.yadan.saleticket.entity.*;
import com.yadan.saleticket.model.Member;
import com.yadan.saleticket.model.WxMaJscode2Session;
import com.yadan.saleticket.service.user.AppMemberService;
import com.yadan.saleticket.service.user.SmsVerifyService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user/open")
@Slf4j
public class WxMaUserController {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private WxMaJscode2SessionRepository wxMaJscode2SessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AppMemberService appMemberService;

    @Autowired
    private STRememberMeService stRememberMeService;

    @Autowired
    private SmsVerifyService smsVerifyService;

    /**
     * 登陆接口
     */
    @ApiOperation("微信通过code授权")
    @PostMapping("/auth")
    public AppMemberVO login(
            @ApiIgnore HttpServletRequest request,
            @ApiIgnore HttpServletResponse response,
            @Valid @RequestBody WxLoginReqVO wxLoginReqVO) {
        AppMemberVO appMemberVO = new AppMemberVO();
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(wxLoginReqVO.getCode());
            WxMaJscode2Session existedSession = wxMaJscode2SessionRepository.findWxMaJscode2SessionByOpenIdAndSessionKey(session.getOpenid(), session.getSessionKey());
            if (existedSession == null) {
                WxMaJscode2Session newSession = new WxMaJscode2Session();
                newSession.setOpenId(session.getOpenid());
                newSession.setSessionKey(session.getSessionKey());
                wxMaJscode2SessionRepository.save(newSession);

                appMemberVO.setOpenId(session.getOpenid());
            } else {
                Member member = memberRepository.findMemberByOpenIdAndSessionKey(session.getOpenid(), session.getSessionKey());
                stRememberMeService.createToken(request, response, member, -1);

                appMemberService.from(member);
            }
        } catch (WxErrorException e) {
            throw new ServiceException(ExceptionCode.WX_LOGIN_ERROR, e.getMessage());
        }
        return appMemberVO;
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
    public String sendSms(@Valid @RequestBody AppSendSmsReqVO appSendSmsReqVO) {
        String code = smsVerifyService.sendSmsVerify(appSendSmsReqVO.getMobile());
        return code;
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public AppMemberVO register(@ApiIgnore HttpServletRequest request,
                                @ApiIgnore HttpServletResponse response,
                                @Valid @RequestBody WxRegisterReqVO wxRegisterReqVO) {
        // 验证验证码
        smsVerifyService.validSmsVerify(wxRegisterReqVO.getMobile(), wxRegisterReqVO.getCode());

        WxMaJscode2Session session = wxMaJscode2SessionRepository.findWxMaJscode2SessionByOpenId(wxRegisterReqVO.getOpenId());
        if (session == null) {
            throw new ServiceException(ExceptionCode.WX_LOGIN_ERROR, "wxSession获取失败，请重新授权");
        }
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(session.getSessionKey(), wxRegisterReqVO.getEncryptedData(), wxRegisterReqVO.getIv());

        Member member = appMemberService.register(wxRegisterReqVO.getMobile(), userInfo, session);
        stRememberMeService.createToken(request, response, member, -1);
        return appMemberService.from(member);
    }

}
