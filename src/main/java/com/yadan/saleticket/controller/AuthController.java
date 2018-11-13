package com.yadan.saleticket.controller;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于控制Spring Security登录页面
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping("/success")
    public String success() {
        return "success";
    }

    @RequestMapping("/needLogin")
    public void needLogin() {
        throw new ServiceException(ExceptionCode.NO_PERMISSION, "token过期或者不存在");
    }

    @RequestMapping("/failure")
    public String failure() {
        return "failure";
    }

    @RequestMapping("/deny")
    public void deny() {
        throw new ServiceException(ExceptionCode.NO_PERMISSION, "用户权限不够");
    }

}
