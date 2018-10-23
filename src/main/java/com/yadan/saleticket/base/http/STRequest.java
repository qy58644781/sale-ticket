package com.yadan.saleticket.base.http;

/**
 * Created by wujun on 2018/2/7.
 * 本次请求包含的内容
 */
public class STRequest {

    /**
     * 本次请求的id
     */
    public static final ThreadLocal<String> REQUEST_ID = new ThreadLocal();

    /**
     * 接口调用的服务
     */
    public static final ThreadLocal<String> SERVICE = new ThreadLocal();

    /**
     * 接口调用的方法
     */
    public static final ThreadLocal<String> METHOD = new ThreadLocal();

    /**
     * 调用接口的远程地址
     */
    public static final ThreadLocal<String> REMOTE_IP = new ThreadLocal();

    /**
     * 请求的json对象
     */
    public static final ThreadLocal<String> REQUEST_JSON_BODY = new ThreadLocal<>();

}
