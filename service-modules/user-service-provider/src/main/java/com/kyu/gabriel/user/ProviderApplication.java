package com.kyu.gabriel.user;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"com.kyu.gabriel.user.dao", "com.kyu.gabriel.model"})
@SpringBootApplication
@ComponentScan(value = {"com.kyu.gabriel.core", "com.kyu.gabriel.user"})
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableAutoDataSourceProxy
public class ProviderApplication {

    public static void main(String[] args){
        SpringApplication.run(ProviderApplication.class, args);
    }
}
