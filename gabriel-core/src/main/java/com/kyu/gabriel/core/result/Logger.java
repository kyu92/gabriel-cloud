package com.kyu.gabriel.core.result;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logger {

    String value();

    String []exclude() default {};

    Class<?> []excludeClass() default {
            HttpServletRequest.class,
            HttpServletResponse.class,
            MultipartFile.class
    };

    boolean excludeResponseData() default false;
}
