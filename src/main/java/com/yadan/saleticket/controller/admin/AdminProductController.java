package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.dao.hibernate.UserLoginTokenLogRepository;
import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.model.user.User;
import com.yadan.saleticket.model.user.UserLoginTokenLog;
import com.yadan.saleticket.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private UserLoginTokenLogRepository userLoginTokenLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmsService smsService;

    @RequestMapping("/open/test1")
    public String test() {
//        RedisHelper.writeValue("1", 123);
//        smsService.sendSms("86", "13816978323", "123");
        return "i am open";
    }

    @RequestMapping("/test2")
    public String test2() {
        return "i am not open";
    }
}
