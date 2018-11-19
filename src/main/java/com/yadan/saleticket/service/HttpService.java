package com.yadan.saleticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpService {

    @Autowired
    private RestTemplate restTemplate;

    public void doGet() {

    }

    public <T> T postForObject(String url, HttpEntity request, Class<T> responseType) {
        T t = restTemplate.postForObject(url, request, responseType);
        return t;
    }
}
