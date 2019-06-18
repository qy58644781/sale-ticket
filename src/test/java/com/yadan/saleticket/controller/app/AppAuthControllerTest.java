package com.yadan.saleticket.controller.app;

import com.yadan.saleticket.controller.BaseControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


public class AppAuthControllerTest extends BaseControllerTest {

    @Test
    public void sendSms() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/app/auth/sendSms")
                .param("mobile", "13816978323")
                .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(status);
        System.out.println(content);
    }

    @Test
    public void register() {
    }

    @Test
    public void login() {
    }

    @Test
    public void loginBySms() {
    }
}