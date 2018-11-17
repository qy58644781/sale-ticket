package com.yadan.saleticket.service;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.dao.hibernate.UserLoginTokenLogRepository;
import com.yadan.saleticket.model.user.UserLoginTokenLog;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLoginTokenLogService {

    @Autowired
    private UserLoginTokenLogRepository userLoginTokenLogRepository;

    /**
     * 保存一条最新的token，将其他token失效
     *
     * @param userId
     * @param token
     */
    @Transactional
    public void saveValidToken(Long userId, String token) {
        // token 入库，作为单点登录依据
        userLoginTokenLogRepository.findAllByUserId(userId)
                .forEach(each -> userLoginTokenLogRepository.delete(each));
        UserLoginTokenLog log = new UserLoginTokenLog();
        log.setUserId(userId);
        log.setHeaderSecurityToken(token);
        userLoginTokenLogRepository.save(log);
    }

    /**
     * 验证cookie中的token是否为最新的那条token
     *
     * @param userId
     * @param cookieToken
     */
    public void checkValidToken(Long userId, String cookieToken) {
        List<UserLoginTokenLog> logs = userLoginTokenLogRepository.findAllByUserId(userId);
        // 单点登录依据：判断当前token是否为最新的token，并且与最新的token值相等
        if (CollectionUtils.isEmpty(logs)
                || logs.size() > 1
                || !logs.get(0).getHeaderSecurityToken().equals(cookieToken)) {
            throw new ServiceException(ExceptionCode.NO_PERMISSION, "token过期或者不存在");
        }
    }
}
