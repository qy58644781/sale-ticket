package com.yadan.saleticket.controller.app;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.security.STRememberMeService;
import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.model.user.User;
import com.yadan.saleticket.service.AppUserService;
import com.yadan.saleticket.service.SmsVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/app/auth")
@RestController
public class AppAuthController {

    @Autowired
    private SmsVerifyService smsVerifyService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private STRememberMeService stRememberMeService;

    /**
     * 账号密码登录
     *
     * @param mobile
     * @param password
     */
    @RequestMapping("/login")
    public void login(HttpServletRequest request,
                      HttpServletResponse response,
                      String mobile, String password) {
        User user = userRepository.findUserByMobileAndPassword(mobile, password);
        if (user == null) {
            throw new ServiceException(ExceptionCode.INVALID_USER, "用户手机号不存在或者密码错误");
        }
        stRememberMeService.createToken(request, response, user, 0);
    }

    /**
     * 发送短信验证码
     */
    @RequestMapping("/sendSms")
    public String sendSms(String mobile) {
        // todo 为了测试方便返回code，后续不能返回
        String code = smsVerifyService.sendSmsVerify(mobile);
        return code;
    }

    /**
     * 短信登录/注册
     *
     * @param mobile
     * @param code
     */
    @RequestMapping("/loginBySms")
    public void loginBySms(HttpServletRequest request,
                           HttpServletResponse response,
                           String mobile, String code) {
        smsVerifyService.validSmsVerify(mobile, code);
        User persistUser = appUserService.findOrRegister(mobile);
        stRememberMeService.createToken(request, response, persistUser, 0);
    }
}
