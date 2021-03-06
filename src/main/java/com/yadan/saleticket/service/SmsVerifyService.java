package com.yadan.saleticket.service;

import com.yadan.saleticket.base.exception.ExceptionCode;
import com.yadan.saleticket.base.exception.ServiceException;
import com.yadan.saleticket.base.tools.CommonUtils;
import com.yadan.saleticket.dao.hibernate.SmsVerifyRepository;
import com.yadan.saleticket.model.SmsVerify;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsVerifyService {
    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsVerifyRepository smsVerifyRepository;

    @Value("${buisness.smsCodeValidSeconds}")
    private Integer smsCodeValidSeconds;

    @Value("${buisness.smsRecentCodeSeconds}")
    private Integer smsRecentCodeSeconds;

    /**
     * 发送验证码
     *
     * @param mobile
     */
    public String sendSmsVerify(String mobile) {
        String verifyCode = CommonUtils.genRandomNumCode(4);
        List<SmsVerify> smsVerifies = smsVerifyRepository.findRecentCode(mobile, smsRecentCodeSeconds);
        if (CollectionUtils.isNotEmpty(smsVerifies)) {
            throw new ServiceException(ExceptionCode.INVALID_CODE, "请勿在" + smsRecentCodeSeconds + "秒内重复发送短信");
        }
        smsService.sendSms("86", mobile, verifyCode);
        SmsVerify smsVerify = new SmsVerify();
        smsVerify.setCode(verifyCode);
        smsVerify.setMobile(mobile);
        smsVerify.setValid(true);
        smsVerifyRepository.save(smsVerify);

        return verifyCode;
    }

    /**
     * 验证 验证码
     *
     * @param mobile
     * @param code
     * @return
     */
    public void validSmsVerify(String mobile, String code) {
        List<SmsVerify> smsVerifies = smsVerifyRepository.findValid(mobile, code, smsCodeValidSeconds);
        if (CollectionUtils.isEmpty(smsVerifies)) {
            throw new ServiceException(ExceptionCode.INVALID_CODE, "验证码无效");
        }
        smsVerifies.forEach(each -> {
            each.setValid(false);
            smsVerifyRepository.save(each);
        });
    }
}
