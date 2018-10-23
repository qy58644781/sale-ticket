package com.yadan.saleticket.controller;


import com.yadan.saleticket.base.http.STResponse;
import com.yadan.saleticket.base.http.handler.JSONFilter;
import com.yadan.saleticket.dao.UserRepository;
import com.yadan.saleticket.enums.Sex;
import com.yadan.saleticket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/addUser")
    public void addUser() {
        User u = new User();
        u.setMobile("123456");
        u.setNickname("nico");
        u.setSex(Sex.MALE);
        userRepository.merge(u);
    }


    @RequestMapping("/updateUser")
    public void updateUser(){
//        User user = userRepository.findUserByMobile("123456");
//        user.setNickname(null);
//        user.setNickname("heihei");
//        user.setSex(Sex.FEMALE);
        User user =new User();
        user.setId(3L);
        user.setSex(Sex.FEMALE);
        userRepository.merge(user);
//        userRepository.dynamicSave(user.getId(), user);
//        System.out.println(user);
    }

    @RequestMapping("/find")
    public User user() {
        User user = userRepository.findOne(3L);
        return user;
    }
}
