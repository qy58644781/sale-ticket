package com.yadan.saleticket.base.http.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yadan.saleticket.base.http.CustomerMappingJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
@ComponentScan("com.yadan")
public class InterceptRequest implements WebMvcConfigurer {
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
                        && ((HandlerMethod) handler).getBean().getClass().getPackage().getName()
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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();

        ObjectMapper om = new ObjectMapper();
        JavaTimeModule jtm = new JavaTimeModule();
        jtm.addSerializer(LocalDateTime.class, this.myLocalDataTimeJsonSerializer());
        om.registerModule(jtm);


        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);

        converters.add(new CustomerMappingJackson2HttpMessageConverter(om));
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private abstract class IgnoreHibernatePropertiesInJackson {
    }

    private JsonSerializer<LocalDateTime> myLocalDataTimeJsonSerializer() {
        JsonSerializer<LocalDateTime> JsonSerializer = new JsonSerializer() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                if (o != null) {
                    jsonGenerator.writeNumber(Timestamp.valueOf((LocalDateTime) o).getTime());
                }

            }
        };
        return JsonSerializer;
    }
}
