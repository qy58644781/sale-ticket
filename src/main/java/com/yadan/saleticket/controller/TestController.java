package com.yadan.saleticket.controller;


import com.alibaba.fastjson.JSONObject;
import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.user.User;
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
        u.setSexEnum(SexEnum.MALE);
        userRepository.merge(u);
    }

    @RequestMapping("/job/notification")
    public JSONObject test(String date, String name, Integer status, String key) {
        System.out.println(date);
        System.out.println(name);
        System.out.println(status);
        System.out.println(key);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", key);
        jsonObject.put("createtime", "2011-01-01 11:11:11");
        return jsonObject;
    }

    @RequestMapping("/updateUser")
    public void updateUser() {
//        User user = userRepository.findUserByMobile("123456");
//        user.setNickname(null);
//        user.setNickname("heihei");
//        user.setSex(Sex.FEMALE);

        User user = new User();
        user.setId(3L);
        user.setSexEnum(SexEnum.FEMALE);
        userRepository.merge(user);
//        userRepository.dynamicSave(user.getId(), user);
//        System.out.println(user);
    }

    @RequestMapping("/find")
    public User user() {
        User user = userRepository.findOne(3L);
        return user;
    }

    public static void main(String[] args) {
        String site = "{[2,0,0,3],\n" +
                "[2,0,0,3],\n" +
                "[2,0,0,3],\n" +
                "[7]}";
    }
}
