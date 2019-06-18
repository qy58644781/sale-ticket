package com.yadan.saleticket.controller.app;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.security.STRememberMeService;
import com.yadan.saleticket.base.tools.MD5;
import com.yadan.saleticket.dao.hibernate.MemberRepository;
import com.yadan.saleticket.entity.*;
import com.yadan.saleticket.model.Member;
import com.yadan.saleticket.service.user.AppMemberService;
import com.yadan.saleticket.service.user.SmsVerifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(description = "app用户登录模块")
@RequestMapping("/app/auth/open")
@RestController
public class AppAuthController {

    @Autowired
    private SmsVerifyService smsVerifyService;

    @Autowired
    private AppMemberService appMemberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private STRememberMeService stRememberMeService;

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
                                @Valid @RequestBody AppRegisterReqVO appRegisterReqVO) {
        smsVerifyService.validSmsVerify(appRegisterReqVO.getMobile(), appRegisterReqVO.getCode());
        Member member = appMemberService.register(appRegisterReqVO.getMobile(), appRegisterReqVO.getMobile());
        stRememberMeService.createToken(request, response, member, -1);
        return appMemberService.from(member);
    }

    @ApiOperation("账号密码登录")
    @PostMapping("/loginByPassword")
    public AppMemberVO login(HttpServletRequest request,
                             HttpServletResponse response,
                             @Valid @RequestBody AppLoginByPsdReqVO appLoginByPsdReqVO) {
        Member member = memberRepository.findMemberByMobileAndPassword(appLoginByPsdReqVO.getMobile(), MD5.MD5Encode(appLoginByPsdReqVO.getPassword()));
        if (member == null) {
            throw new ServiceException(ExceptionCode.INVALID_USER, "用户手机号不存在或者密码错误");
        }
        stRememberMeService.createToken(request, response, member, -1);
        return appMemberService.from(member);
    }

    @ApiOperation("短信登录")
    @PostMapping("/loginBySms")
    public AppMemberVO loginBySms(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @Valid @RequestBody AppLoginBySmsReqVO appLoginBySmsReqVO) {
        smsVerifyService.validSmsVerify(appLoginBySmsReqVO.getMobile(), appLoginBySmsReqVO.getCode());
        Member member = memberRepository.findMemberByMobile(appLoginBySmsReqVO.getMobile());
        if (member == null) {
            throw new ServiceException(ExceptionCode.INVALID_USER, "用户手机号不存在");
        }
        stRememberMeService.createToken(request, response, member, -1);
        return appMemberService.from(member);
    }


}
