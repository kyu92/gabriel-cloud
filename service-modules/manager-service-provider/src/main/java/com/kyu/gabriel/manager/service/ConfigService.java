package com.kyu.gabriel.manager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kyu.gabriel.core.model.po.manager.Config;
import com.kyu.gabriel.manager.dao.ConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService {

    private final ConfigDao configDao;

    @Autowired
    public ConfigService(ConfigDao configDao){
        this.configDao = configDao;
    }

    public List<Config> listConfig(){
        QueryWrapper<Config> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        return configDao.selectList(wrapper);
    }

    public boolean updateConfig(List<Config> configList){
        return configDao.updateBatch(configList) / 2 == configList.size();
    }

    public Config getConfigOption(String configKey){
        QueryWrapper<Config> wrapper = new QueryWrapper<>();
        wrapper.eq("config_key", configKey);
        return configDao.selectOne(wrapper);
    }

    public Config getConfigOption(int id){
        return configDao.selectById(id);
    }
}
