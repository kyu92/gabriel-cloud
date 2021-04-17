package com.kyu.gabriel.manager.controller;

import com.kyu.gabriel.core.cache.Cache;
import com.kyu.gabriel.core.cache.Empty;
import com.kyu.gabriel.core.model.po.manager.Config;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService){
        this.configService = configService;
    }

    @GetMapping("/")
    @Cache(value = "config", time = -1)
    public ResultMap<List<Config>> getConfig(){
        return ResultMap.success(configService.listConfig());
    }

    @PostMapping("/")
    @Empty("config")
    public ResultMap<Void> updateConfig(@RequestBody List<Config> config){
        if (configService.updateConfig(config)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入异常");
    }

    @GetMapping("/key/{configKey}")
    @Cache(value = "config:option", time = -1)
    public ResultMap<Config> getConfigByKey(@PathVariable String configKey){
        return ResultMap.success(configService.getConfigOption(configKey));
    }

    @GetMapping("/id/{configId}")
    @Cache(value = "config:option", time = -1)
    public ResultMap<Config> getConfigById(@PathVariable int configId){
        return ResultMap.success(configService.getConfigOption(configId));
    }
}
