package com.yadan.saleticket.base.security;

import com.yadan.saleticket.model.User;
import com.yadan.saleticket.service.UserLoginTokenLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class STRememberMeService extends TokenBasedRememberMeServices {

    protected final String HEADER_SECURITY_TOKEN = "HEADER_SECURITY_TOKEN";

    @Autowired
    private UserLoginTokenLogService userLoginTokenLogService;

    @Override
    @Value("true")
    public void setAlwaysRemember(boolean alwaysRemember) {
        super.setAlwaysRemember(alwaysRemember);
    }

    public STRememberMeService(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected String extractRememberMeCookie(HttpServletRequest request) {
        return StringUtils.isEmpty(request.getHeader(HEADER_SECURITY_TOKEN))
                ? request.getParameter(HEADER_SECURITY_TOKEN)
                : request.getHeader(HEADER_SECURITY_TOKEN);
    }

    /**
     * 设置浏览器cookie，这里直接跳过对cookie的操作，仅返回token
     * @param tokens
     * @param maxAge
     * @param request
     * @param response
     */
    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        response.setHeader(HEADER_SECURITY_TOKEN, cookieValue);
    }

    /**
     * token 生成
     *
     * @param request
     * @param response
     * @param successfulAuthentication
     */
    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                               Authentication successfulAuthentication) {
        User user = ((User) ((AuthenticationUserWrapper) (successfulAuthentication.getPrincipal())).getUser());
        int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
        this.createToken(request, response, user, tokenLifetime);

    }

    /**
     * 判断用户账号密码正确以后 生成token
     * @param request
     * @param response
     * @param user
     * @param tokenLifetime
     */
    public void createToken(HttpServletRequest request, HttpServletResponse response,
                            User user, int tokenLifetime) {
        String username = user.getMobile();
        String password = user.getPassword();

        if (!org.springframework.util.StringUtils.hasLength(username)) {
            logger.debug("Unable to retrieve username");
            return;
        }

        long expiryTime = System.currentTimeMillis();
        expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);

        String signatureValue = makeTokenSignature(expiryTime, username, password);

        String[] tokens = new String[]{username, Long.toString(expiryTime), signatureValue};
        setCookie(tokens, tokenLifetime, request, response);

        HeaderSecurityTokenEnum tokenType = HeaderSecurityTokenEnum.getPathEnum(request.getServletPath());
        userLoginTokenLogService.saveValidToken(user, encodeCookie(tokens), tokenType);

        if (logger.isDebugEnabled()) {
            logger.debug("Added remember-me cookie for user '" + username + "', expiry: '"
                    + new Date(expiryTime) + "'");
        }
    }

    /**
     * token 验证
     *
     * @param cookieTokens
     * @param request
     * @param response
     * @return
     */
    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens,
                                                 HttpServletRequest request, HttpServletResponse response) {
        AuthenticationUserWrapper<User> userDetails = (AuthenticationUserWrapper) (super.processAutoLoginCookie(cookieTokens, request, response));

        HeaderSecurityTokenEnum tokenType = HeaderSecurityTokenEnum.getPathEnum(request.getServletPath());
        userLoginTokenLogService.checkValidToken(userDetails.getUser(), encodeCookie(cookieTokens), tokenType);

        return userDetails;
    }

}