package com.kyu.gabriel.manager.controller;

import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.file.MinioUtil;
import com.kyu.gabriel.core.result.Logger;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.ManagerService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class TestController {

    @Autowired
    private ManagerService managerService;
    @Autowired
    private ConfigMap configMap;

    @Logger("测试")
    @RequestMapping("/test/log")
    public ResultMap<Void> logTest(){
        return ResultMap.success();
    }

    @RequestMapping("/test/config")
    public ResultMap<ConfigMap> testConfig(){
        return ResultMap.success(configMap);
    }

    @RequestMapping("/test/config/minio")
    public ResultMap<Void> testMinioConfig() throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InsufficientDataException, InternalException {
        MinioUtil minioUtil = MinioUtil.getInstance(configMap);
        minioUtil.isBucketExists("aaa");
        return ResultMap.success();
    }
}
