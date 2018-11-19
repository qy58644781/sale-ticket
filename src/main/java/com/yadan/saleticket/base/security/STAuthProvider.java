package com.yadan.saleticket.base.security;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.tools.MD5;
import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Setter
@Getter
@Component
public class STAuthProvider implements AuthenticationProvider {
    @Autowired
    private STUserDetailsService STUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        String password = (String) authentication.getCredentials();

        User stUser = userRepository.findUserByMobileAndPassword(username, MD5.MD5Encode(password));
        if (stUser == null) {
            throw new ServiceException(ExceptionCode.NO_PERMISSION, "用户账号或密码错误");
        }

        UserDetails userDetails = this.getSTUserDetailsService().loadUserByUsername(username);

        return this.createSuccessAuthentication(userDetails, authentication, userDetails);
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails user) {
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal,
                authentication.getCredentials(), user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }


}