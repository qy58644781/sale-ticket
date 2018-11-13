package com.yadan.saleticket.base.security;

import com.yadan.saleticket.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


public class STRememberMeService extends TokenBasedRememberMeServices {

    protected final String HEADER_SECURITY_TOKEN = "HEADER_SECURITY_TOKEN";

    @Autowired
    private SecurityService securityService;

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

    @Override
    protected boolean isTokenExpired(long tokenExpiryTime) {
        return false;
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        response.setHeader(HEADER_SECURITY_TOKEN, cookieValue);
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                               Authentication successfulAuthentication) {
        User user = ((User) ((AuthenticationUserWrapper) (successfulAuthentication.getPrincipal())).getUser());
        String username = user.getMobile();
        String password = securityService.getCurrentLoginUser().getPassword();
        //String password = user.getOpenId();

        if (!org.springframework.util.StringUtils.hasLength(username)) {
            logger.debug("Unable to retrieve username");
            return;
        }

        int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
        long expiryTime = System.currentTimeMillis();
        expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);

        String signatureValue = makeTokenSignature(expiryTime, username, password);

        setCookie(new String[]{username, Long.toString(expiryTime), signatureValue}, tokenLifetime, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("Added remember-me cookie for user '" + username + "', expiry: '"
                    + new Date(expiryTime) + "'");
        }
    }

    public void genOdmToken(HttpServletRequest request, HttpServletResponse response, String username, String password) {

        if (!org.springframework.util.StringUtils.hasLength(username)) {
            logger.debug("Unable to retrieve username");
            return;
        }


        long expiryTime = System.currentTimeMillis();
        expiryTime += 1000L * TWO_WEEKS_S;

        String signatureValue = makeTokenSignature(expiryTime, username, password);

        setCookie(new String[]{username, Long.toString(expiryTime), signatureValue}, 0, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("Added remember-me cookie for user '" + username + "', expiry: '"
                    + new Date(expiryTime) + "'");
        }

    }

}