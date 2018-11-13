package com.yadan.saleticket.controller.app;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/app/auth")
public class AppAuthController {

    @RequestMapping("/login")
    public void login(String username, String password) {

    }

    @RequestMapping("/loginBySms")
    public void loginBySms(String username, String sms){

    }
}
