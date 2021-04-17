package com.kyu.gabriel.core.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ConfigMapBean {

    @Bean
    public ConfigMap configMap(ApplicationContext context) {
        boolean hasBean = Arrays.stream(context.getBeanDefinitionNames()).anyMatch(s -> s.contains("ManagerService"));
        if(hasBean) {
            return new ConfigMap(context);
        }
        return null;
    }
}
