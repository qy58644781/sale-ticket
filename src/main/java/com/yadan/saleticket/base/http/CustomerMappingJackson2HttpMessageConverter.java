package com.yadan.saleticket.base.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yadan.saleticket.base.tools.Json;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

public class CustomerMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public CustomerMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Object read = super.read(type, contextClass, inputMessage);
        STRequest.REQUEST_JSON_BODY.set(Json.Object2String(read));
        return read;
    }
}
