package com.yadan.saleticket.base.http.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@ComponentScan("com.yadan")
public class InterceptRequest extends WebMvcConfigurerAdapter {
    @Autowired
    private ProcessRequest processRequest;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor());
    }

    @Bean
    public HandlerInterceptorAdapter requestInterceptor() {
        return new HandlerInterceptorAdapter() {

            public boolean isSupport(Object handler) {
                if (handler instanceof HandlerMethod
                        && ((HandlerMethod)handler).getBean().getClass().getPackage().getName()
                        .contains("com.yadan")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
                if (this.isSupport(handler)) {
                    processRequest.clearRequestContext();
                    processRequest.setRequestContext(request, (HandlerMethod) handler);
                }
                return true;
            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                if (this.isSupport(handler)) {
                    processRequest.printLog(request, response);
                }
            }
        };
    }
}
