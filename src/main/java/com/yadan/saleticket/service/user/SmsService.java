package com.yadan.saleticket.service.user;

import com.alibaba.fastjson.JSONObject;
import com.yadan.saleticket.base.AppConfig;
import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.service.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.ACCEPT_CHARSET;

@Service
@Slf4j
public class SmsService {
    @Autowired
    private AppConfig.Sms sms;
    @Autowired
    private AppConfig.Sms.Submail submail;
    @Autowired
    private HttpService httpService;


    public boolean sendSms(String phoneCode, String to, String content) {
        boolean isSuccess = sendSmsUseSubmail(to, content);
        log.info("短信发送[result=" + isSuccess + "]:phoneCode=[" + phoneCode + "],to=[" + to + "],content=[" + content + "]");
        return isSuccess;
    }

    public void sendSms(String phoneCode, String to, String templateCode, JSONObject jsonObject) {
        boolean success = sendSmsUseSubmail(to, templateCode, jsonObject);
        log.info("短信发送[result=" + success + "]:phoneCode=[" + phoneCode + "],to=[" + to + "],templateCode=[" + templateCode + "],jsonVars=[" + jsonObject + "]");
    }


    private boolean sendSmsUseSubmail(String to, String content) {
        if (!sms.getTurnOn()) {
            return true;
        }
        boolean success = false;
        JSONObject smsVars = new JSONObject();
        smsVars.put("express", content);

        Map map = new HashMap<>();
        map.put("to", to);
        map.put("project", submail.getProject());
        map.put("appid", submail.getAppid());
        map.put("signature", submail.getSignature());
        map.put("sign_type", submail.getSigntype());
        map.put("vars", smsVars.toJSONString());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(ACCEPT_CHARSET, "UTF-8");
        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);

        LinkedHashMap response = httpService.postForObject(submail.getPath(), httpEntity, LinkedHashMap.class);
        if (response != null && "success".equals(response.get("status"))) {
            success = true;
            return success;
        }
        System.out.println(response);
        throw new ServiceException(ExceptionCode.MOBILEFORMATERROR, "手机号码格式不正确");
    }

    private boolean sendSmsUseSubmail(String to, String templateCode, JSONObject JSONObject) {
        if (!sms.getTurnOn()) {
            return true;
        }
        boolean success = false;

        Map map = new HashMap<>();
        map.put("to", to);
        map.put("project", templateCode);
        map.put("appid", submail.getAppid());
        map.put("signature", submail.getSignature());
        map.put("sign_type", submail.getSigntype());
        map.put("vars", JSONObject == null ? new JSONObject().toString() : JSONObject.toString());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(ACCEPT_CHARSET, "UTF-8");
        HttpEntity httpEntity = new HttpEntity(map, httpHeaders);

        LinkedHashMap response = httpService.postForObject(submail.getPath(), httpEntity, LinkedHashMap.class);
        if (response != null && Boolean.TRUE.equals(response.get("status"))) {
            success = true;
        }

        return success;
    }
}
