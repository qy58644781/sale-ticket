package com.yadan.saleticket.base.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class Json {
    public static ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(Json.class.getName());

    /**
     * 类或者集合转成json字符串
     */
    public static <T> String Object2String(T object) {

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MessageFormat.format("对象转String发生错误", e.getMessage()));
        }
        return null;
    }

    /**
     * json字符串转成普通对象
     */
    public static <T> T String2Object(String json, Class<T> object) {

        try {
            return objectMapper.readValue(json, object);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MessageFormat.format("String转对象发生错误", e.getMessage()));
        }

        return null;
    }

    /**
     * json字符串转成集合对象
     * 使用示例：String2Collection(param, new TypeReference<List<User>>() {})
     */
    public static <T> T String2Collection(String json, TypeReference<T> typeReference) {

        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MessageFormat.format("String转集合发生错误", e.getMessage()));
        }

        return null;
    }
}
