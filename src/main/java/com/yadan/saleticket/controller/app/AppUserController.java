package com.yadan.saleticket.controller.app;

import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/user")
public class AppUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/open/users")
    public Page<User> openUsers(Integer page, Integer count) {
        Page<User> users = userRepository.findAll(new PageRequest(page - 1, count));
        return users;
    }

    @GetMapping("/users")
    public Page<User> users(Integer page, Integer count) {
        Page<User> users = userRepository.findAll(new PageRequest(page - 1, count));
        return users;
    }

    public void update() {

    }

    public void detail() {

    }

    @Transactional
    @GetMapping("/open/addUser")
    public void addUser() {
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setNickname("我是" + i);
            user.setMobile(123 + "" + i);
            user.setPassword(user.getMobile());
            user.setSexEnum(SexEnum.UNKNOW);
            userRepository.save(user);
        }
    }

}
