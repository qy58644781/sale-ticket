package com.yadan.saleticket;

import com.yadan.saleticket.dao.base.ExtJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by wujun on 2017/11/10.
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = ExtJpaRepositoryFactoryBean.class)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}

