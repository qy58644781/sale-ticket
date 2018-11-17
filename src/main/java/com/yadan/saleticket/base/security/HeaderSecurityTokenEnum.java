package com.yadan.saleticket.base.security;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by wujun on 2018/2/6.
 */
public enum HeaderSecurityTokenEnum {
    /**
     * 会员
     */
    ST_MEMBER,

    /**
     * 后台管理员
     */
    ST_ADMIN;

    // todo 优化逻辑
    public static HeaderSecurityTokenEnum getPathEnum(String path) {
        if (StringUtils.isEmpty(path))
            throw new ServiceException(ExceptionCode.SYSTEM, "错误的url");

        final String app = "^/app/.+$";
        final String admin = "^/admin/.+$";

        if (Pattern.matches(app, path))
            return ST_MEMBER;
        else if (Pattern.matches(admin, path))
            return ST_ADMIN;
        else
            throw new ServiceException(ExceptionCode.SYSTEM, "错误的url");
    }
}
