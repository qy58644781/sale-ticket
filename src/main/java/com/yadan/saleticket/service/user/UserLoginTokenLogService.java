package com.yadan.saleticket.service.user;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.security.HeaderSecurityTokenEnum;
import com.yadan.saleticket.dao.hibernate.UserLoginTokenLogRepository;
import com.yadan.saleticket.model.user.User;
import com.yadan.saleticket.model.user.UserLoginTokenLog;
import org.apache.commons.collections4.CollectionUtils;
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
     * @param user
     * @param token
     */
    @Transactional
    public void saveValidToken(User user, String token, HeaderSecurityTokenEnum tokenType) {
        // token 入库，作为单点登录依据
        userLoginTokenLogRepository.findAllByUserAndTokenType(user, tokenType)
                .forEach(each -> userLoginTokenLogRepository.delete(each));

        UserLoginTokenLog log = new UserLoginTokenLog();
        log.setUser(user);
        log.setHeaderSecurityToken(token);
        log.setTokenType(tokenType);
        userLoginTokenLogRepository.save(log);
    }

    /**
     * 验证cookie中的token是否为最新的那条token
     *
     * @param user
     * @param cookieToken
     */
    public void checkValidToken(User user, String cookieToken, HeaderSecurityTokenEnum tokenType) {
        List<UserLoginTokenLog> logs = userLoginTokenLogRepository.findAllByUserAndTokenType(user, tokenType);
        // 单点登录依据：判断当前token是否为最新的token，并且与最新的token值相等
        if (CollectionUtils.isEmpty(logs)
                || logs.size() > 1
                || !logs.get(0).getHeaderSecurityToken().equals(cookieToken)) {
            throw new ServiceException(ExceptionCode.NO_PERMISSION, "token过期或者不存在");
        }
    }
}
