package com.kyu.gabriel.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyu.gabriel.core.model.po.manager.Log;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDao extends BaseMapper<Log> {
}
