package com.kyu.gabriel.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyu.gabriel.core.model.po.manager.Config;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ConfigDao extends BaseMapper<Config> {

    int updateBatch(Collection<? extends Config> configList);
}
