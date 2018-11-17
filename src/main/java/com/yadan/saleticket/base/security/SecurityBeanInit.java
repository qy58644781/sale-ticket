package com.yadan.saleticket.base.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class SecurityBeanInit {

    /**
     * 用于解析rememberMe的服务类
     *
     * @return
     */
    @Bean
    public STRememberMeService odmRememberMeService(@Value("${global.security.rememberMeKey}") String rememberMeKey,
                                                     @Autowired STUserDetailsService odmUserDetailsService) {
        return new STRememberMeService(rememberMeKey, odmUserDetailsService);
    }
}
