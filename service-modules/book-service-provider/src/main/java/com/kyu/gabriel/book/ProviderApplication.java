package com.kyu.gabriel.book;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"com.kyu.gabriel.book.dao", "com.kyu.gabriel.model"})
@SpringBootApplication
@ComponentScan(value = {"com.kyu.gabriel.core", "com.kyu.gabriel.book"})
@EnableTransactionManagement
@EnableDiscoveryClient
public class ProviderApplication {

    public static void main(String[] args){
        SpringApplication.run(ProviderApplication.class, args);
    }
}
