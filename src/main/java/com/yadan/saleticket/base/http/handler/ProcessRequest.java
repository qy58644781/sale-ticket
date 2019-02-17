package com.yadan.saleticket.base.http.handler;


import com.yadan.saleticket.base.http.STRequest;
import com.yadan.saleticket.base.security.SecurityService;
import com.yadan.saleticket.base.tools.Ip;
import com.yadan.saleticket.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Component
@Slf4j
public class ProcessRequest {
    @Autowired
    private SecurityService securityService;

    public void clearRequestContext() {
        STRequest.REQUEST_ID.remove();
        STRequest.SERVICE.remove();
        STRequest.METHOD.remove();
        STRequest.REMOTE_IP.remove();
    }

    public void setRequestContext(HttpServletRequest request, HandlerMethod handlerMethod) {
        String service = StringUtils.uncapitalize(handlerMethod.getBeanType().getSimpleName().replace("Controller", ""));
        String method = handlerMethod.getMethod().getName();

        String locale = request.getHeader("locale");
        STRequest.REQUEST_ID.set(UUID.randomUUID().toString());
        STRequest.SERVICE.set(service);
        STRequest.METHOD.set(method);
        STRequest.REMOTE_IP.set(Ip.getRemoteIp(request));

        request.setAttribute("startTimeMillis", System.currentTimeMillis());

    }

    /**
     * 打印日志
     */
    public void printLog(HttpServletRequest request, HttpServletResponse response) {

        Long timeMillis = (Long) request.getAttribute("startTimeMillis");
        User user = securityService.getCurrentLoginUser();
        Map map = new LinkedHashMap<>();
        map.put("time", timeMillis);
        map.put("method", request.getMethod());
        map.put("action", request.getRequestURI());
        map.put("locale", LocaleContextHolder.getLocale());
        map.put("cost", System.currentTimeMillis() - timeMillis);
        map.put("user-agent", request.getHeader("user-agent"));
        map.put("header-security-token", request.getHeader("HEADER_SECURITY_TOKEN"));
        map.put("user", user != null ? user.getId() : null);
        map.put("clientIp", STRequest.REMOTE_IP.get());
        map.put("params", handleParameterMap(request));
        map.put("status", response.getStatus());
        map.put("paramsOfJson", STRequest.REQUEST_JSON_BODY.get());

        // 由于Docker分行处理信息的,需要把换行符转义输出.
        log.info(map.toString().replace("\n", "\\n"));

        // todo mq记录日志到数据库
    }

    private Map handleParameterMap(HttpServletRequest request) {
        Map map = new HashMap();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            Object next = enumeration.nextElement();
            String name = next.toString();
            map.put(name, request.getParameter(name));
        }
        return map;
    }
}
