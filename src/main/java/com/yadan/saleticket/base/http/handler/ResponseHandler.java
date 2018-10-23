package com.yadan.saleticket.base.http.handler;


import com.yadan.saleticket.base.http.STRequest;
import com.yadan.saleticket.base.http.STResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@Configuration
@RestControllerAdvice
public class ResponseHandler extends WebMvcConfigurerAdapter implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (!(body instanceof STResponse)) {
            STResponse res = new STResponse();
            res.setError(null);
            res.setResult(body);
            res.setRequestId(STRequest.REQUEST_ID.get());
            return res;
        } else {
            ((STResponse) body).setRequestId(STRequest.REQUEST_ID.get());
            return body;
        }

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
