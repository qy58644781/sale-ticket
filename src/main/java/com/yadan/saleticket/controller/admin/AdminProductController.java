package com.yadan.saleticket.controller.admin;

import com.yadan.saleticket.dao.hibernate.UserLoginTokenLogRepository;
import com.yadan.saleticket.dao.redis.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private UserLoginTokenLogRepository userLoginTokenLogRepository;

    @RequestMapping("/open/test1")
    public String test() {
        RedisHelper.writeValue("1",123);
        return "i am open";
    }

    @RequestMapping("/test2")
    public String test2() {
//        userLoginTokenLogRepository.delete(1L);
        return "i am not open";
    }
}
