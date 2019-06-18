package com.yadan.saleticket.base.security;

import com.yadan.saleticket.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


/**
 * Created by wujun on 2018/2/6.
 */
@Service
public class SecurityService {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public User getCurrentLoginUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthenticationUserWrapper
                && ((AuthenticationUserWrapper) principal).getUser() != null
                && ((AuthenticationUserWrapper) principal).getUser() instanceof User) {
            return (User) ((AuthenticationUserWrapper) principal).getUser();
        } else {
            return null;
        }
    }
}
