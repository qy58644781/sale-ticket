package com.yadan.saleticket.base.http.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.SystemException;
import com.yadan.saleticket.base.http.STResponse;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spring MVC使用了@ResponseBody的JSON转换器,在Spring MVC之中进行配置。
 *
 * @author Cary
 * @date 2016/4/6
 */
@Component
public class MappingJSONHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    @Autowired
    private ProcessRequest processRequest;

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Construct a new {@code MappingJSONHttpMessageConverter}.
     */
    public MappingJSONHttpMessageConverter() {
        super(new MediaType("application", "json", DEFAULT_CHARSET),
                new MediaType("application", "*+json", DEFAULT_CHARSET));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return STResponse.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return STResponse.class.isAssignableFrom(clazz);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        InputStreamReader jsonReader = printJsonLog(inputMessage);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return objectMapper.readValue(jsonReader, clazz);
        } catch (Exception e) {
            throw new SystemException(ExceptionCode.SYSTEM, "解析STResponse对象异常");
        }
    }

    private InputStreamReader printJsonLog(HttpInputMessage inputMessage) throws IOException {
        InputStream input = inputMessage.getBody();
        ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
        byte[] b = new byte[4096];
        int len;
        while ((len = input.read(b)) != -1) {
            byteArr.write(b, 0, len);
        }
        byteArr.flush();

        InputStreamReader logReader = new InputStreamReader(new ByteArrayInputStream(byteArr.toByteArray()));
        BufferedReader br = new BufferedReader(logReader);
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(sb::append);

//        String paramLog = sb.toString();
//        OdmRequest.REQUEST_JSON_BODY.set(paramLog);

        return new InputStreamReader(new ByteArrayInputStream(byteArr.toByteArray()));
    }



    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        if (object instanceof STResponse) {
            writeOdmResponse(object, outputMessage);
        }
        processRequest.clearRequestContext();
    }

    private void writeOdmResponse(Object object, HttpOutputMessage outputMessage) throws IOException {

        STResponse response = (STResponse) object;
        JSONSerializer jsonSerializer = createJSONSerializer(createFilterWithResponser(response));
        String result = jsonSerializer.deepSerialize(response);

        PrintWriter printWriter = new PrintWriter(outputMessage.getBody());
        printWriter.write(result);
        printWriter.flush();
        printWriter.close();
    }

    /**
     * 统一创建JSON生成器
     *
     * @param filter JSON过滤器
     * @return JSONSerializer JSON生成器
     */
    protected JSONSerializer createJSONSerializer(JSONFilter filter) {
        JSONSerializer serializer = new JSONSerializer();
        serializer.prettyPrint(true);
        // 解析LocalDateTIme
        serializer.transform(new AbstractTransformer() {
            @Override
            public Boolean isInline() {
                return false;
            }

            @Override
            public void transform(Object o) {
                getContext().write(this.format((LocalDateTime) o));
            }

            private String format(LocalDateTime date) {
                return String.valueOf(Timestamp.valueOf(date).getTime());
            }

        }, LocalDateTime.class);

        // 解析bigDecimal
//        serializer.transform(new AbstractTransformer() {
//            @Override
//            public Boolean isInline() {
//                return false;
//            }
//            @Override
//            public void transform(Object o) {
//                getContext().writeQuoted(this.format( (BigDecimal) o));
//            }
//
//            private String format(BigDecimal bigDecimal) {
//                return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
//            }
//        }, BigDecimal.class);

        // 去空
        //serializer.transform(new AbstractTransformer() {
        //    @Override
        //    public Boolean isInline() {
        //        return true;
        //    }
        //
        //    @Override
        //    public void transform(Object o) {
        //
        //    }
        //}, Void.TYPE);
        serializer.setIncludes(filter.getIncludes());
        serializer.setExcludes(filter.getExcludes());
        return serializer;
    }

    /**
     * 创建JSON过滤器
     *
     * @param response JSON响应类
     * @return JSONFilter
     */
    protected JSONFilter createFilterWithResponser(STResponse response) {
        List<String> includes = new ArrayList<String>();
        List<String> excludes = new ArrayList<String>();
        JSONFilter filter = new JSONFilter();

        if (response.getJsonFilter() != null) {
            JSONFilter jsonFilter = response.getJsonFilter();
            if (jsonFilter.getIncludes() != null) {
                includes.addAll(jsonFilter.getIncludes());
            }
            if (jsonFilter.getExcludes() != null) {
                excludes.addAll(jsonFilter.getExcludes());
            }
        }

        String[] commentExcludes = new String[]{"*.class"};
        excludes.addAll(Arrays.asList(commentExcludes));

        filter.setExcludes(excludes);
        filter.setIncludes(includes);
        return filter;
    }
}