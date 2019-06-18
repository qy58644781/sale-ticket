package com.yadan.saleticket.base.security;

import com.yadan.saleticket.dao.hibernate.UserRepository;
import com.yadan.saleticket.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static com.yadan.saleticket.base.security.HeaderSecurityTokenEnum.ST_ADMIN;
import static com.yadan.saleticket.base.security.HeaderSecurityTokenEnum.ST_APP;


/**
 * Created by wujun on 2018/2/5.
 */
@Component
@Slf4j
public class STUserDetailsService implements UserDetailsService {


    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("account : " + username + " not found");
        }

        User loginUser = userRepository.findUserByMobile(username);

        if (loginUser == null) {
            throw new UsernameNotFoundException("account : " + username + " not found");
        }
        return new AuthenticationUserWrapper<>(
                loginUser,
                username,
                loginUser.getPassword(),
                this.createGrantedAuthority());
    }

    protected HeaderSecurityTokenEnum getHeaderSecurityTokenEnum() {
        log.info("path: " + httpServletRequest.getServletPath());
        return HeaderSecurityTokenEnum.getPathEnum(httpServletRequest.getServletPath());
    }


    /**
     * 生成权限
     *
     * @return
     */
    private List<GrantedAuthority> createGrantedAuthority() {
        HeaderSecurityTokenEnum headerSecurityTokenEnum = this.getHeaderSecurityTokenEnum();
        log.info("ROLE: " + headerSecurityTokenEnum.toString());
        if (ST_APP.equals(headerSecurityTokenEnum)) {
            return Arrays.asList(new SimpleGrantedAuthority("ST_APP"));
        } else if (ST_ADMIN.equals(headerSecurityTokenEnum)) {
            return Arrays.asList(new SimpleGrantedAuthority("ST_ADMIN"));
        } else {
            return null;
        }
    }
}
