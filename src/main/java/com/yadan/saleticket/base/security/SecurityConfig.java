package com.yadan.saleticket.base.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Created by wujun on 2018/2/5.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private STAuthProvider STAuthProvider;
    @Autowired
    private STRememberMeService stRememberMeService;
    @Value("${global.security.rememberMeKey}")
    private String rememberMeKey;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader(stRememberMeService.HEADER_SECURITY_TOKEN);
        source.registerCorsConfiguration("/**", config);
        http.cors().configurationSource(source);

        // 提供给odm member登录用
        final String loginProcessUrl = "/admin/auth/login";
        final String loginPageUrl = "/auth/needLogin";
        final String successUrl = "/auth/success";
        final String failureUrl = "/auth/failure";

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .accessDecisionManager(stDecisionManager())
                .antMatchers("/auth/**", "/app/**/open/**", "/admin/**/open/**").permitAll()
                .antMatchers("/app/**").access("hasRole('ST_MEMBER')")
                .antMatchers("/admin/**").access("hasRole('ST_ADMIN')")
                .anyRequest().permitAll()

                .and()
                .formLogin()
                .loginPage(loginPageUrl)
                .loginProcessingUrl(loginProcessUrl)
                .successForwardUrl(successUrl)
                .failureForwardUrl(failureUrl)

                .usernameParameter("username")
                .passwordParameter("password")

                .and()
                .rememberMe().rememberMeServices(stRememberMeService)

                .and()
                .authenticationProvider(STAuthProvider)
                .authenticationProvider(new RememberMeAuthenticationProvider(rememberMeKey));

        http.csrf().disable();
    }

    /**
     * 自定义权限前缀
     *
     * @return
     */
    private AccessDecisionManager stDecisionManager() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");

        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(defaultWebSecurityExpressionHandler);

        return new AffirmativeBased(Arrays.asList(webExpressionVoter));
    }

}
