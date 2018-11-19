package com.yadan.saleticket.service;

import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService extends AbstractUserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取用户信息
     *
     */
    public User findOrRegister(String mobile) {
        User user = userRepository.findUserByMobile(mobile);
        if (user == null) {
            user = new User();
            user.setMobile(mobile);
            user.setSexEnum(SexEnum.UNKNOW);
            user.setPassword("");
            user.setNickname(mobile);
            userRepository.save(user);
        }
        return user;
    }
}
