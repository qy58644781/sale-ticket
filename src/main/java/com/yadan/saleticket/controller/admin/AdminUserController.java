package com.yadan.saleticket.controller.admin;


import com.yadan.saleticket.base.tools.BeanUtils;
import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.dao.hibernate.base.STPageRequest;
import com.yadan.saleticket.entity.AddUserVo;
import com.yadan.saleticket.entity.PageVo;
import com.yadan.saleticket.enums.SexEnum;
import com.yadan.saleticket.model.User;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public PageVo<User> users(STPageRequest pageRequest) {
//        PageVo<User> users = userRepository.findAllByFilterAndPageRequest(pageRequest);
//        return users;
        return null;
    }

    @GetMapping("/{id}")
    public User user(@PathVariable("id") Long id) {
        return userRepository.getOne(id);
    }

    @PostMapping("/merge")
    public User merge(@RequestBody AddUserVo user) {
        User saveUser = user.getId() != null ? userRepository.getOne(user.getId()) : new User();
        BeanUtils.copyNotNullProperties(user, saveUser);
        return userRepository.save(saveUser);
    }

    @PostMapping("/delete")
    @Transactional
    public Set<Long> delete(String ids) {
        Set<Long> result = new HashSet<>();
        String[] idArr = ids.split(",");
        if (ArrayUtils.isNotEmpty(idArr)) {
            for (String id : idArr) {
                if (StringUtils.isNotEmpty(id)) {
                    userRepository.deleteById(Long.valueOf(id));
                    result.add(Long.valueOf(id));
                }
            }
        }
        return result;
    }

}
