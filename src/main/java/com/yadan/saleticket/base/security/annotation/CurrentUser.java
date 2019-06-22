package com.yadan.saleticket.base.security.annotation;

import java.lang.annotation.*;

/**
 * class_name: CurrentUser
 * package: com.puer.puercrm.spring.annotation
 * describe: 自定义用户注解
 * creat_user: jay.jia
 * creat_date: 2019/2/25
 * creat_time: 15:39
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    String value() default "";
}

