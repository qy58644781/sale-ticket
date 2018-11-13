package com.yadan.saleticket.base.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 验证用户包装器(业务用户包装)
 *
 * @author cary
 * @date 2016/6/6
 */
public class AuthenticationUserWrapper<T> extends org.springframework.security.core.userdetails.User {
    protected T user;

    public AuthenticationUserWrapper(T user, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, authorities);
        this.user = user;
    }

    public AuthenticationUserWrapper(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthenticationUserWrapper(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }
}
