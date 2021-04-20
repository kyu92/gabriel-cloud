package com.kyu.gabriel.book;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@ComponentScan(value = {"com.kyu.gabriel.core", "com.kyu.gabriel.book"})
@EnableFeignClients
@EnableDiscoveryClient
@EnableRedisHttpSession
@EnableAspectJAutoProxy
@EnableAutoDataSourceProxy
public class ConsumerApplication {

    public static void main(String[] args){
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
