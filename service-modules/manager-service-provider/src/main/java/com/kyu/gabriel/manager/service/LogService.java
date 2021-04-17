package com.kyu.gabriel.manager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyu.gabriel.core.model.dto.PageDTO;
import com.kyu.gabriel.core.model.po.manager.Log;
import com.kyu.gabriel.manager.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LogService {

    private final LogDao logDao;

    @Autowired
    public LogService(LogDao logDao){
        this.logDao = logDao;
    }

    public boolean addLog(Log log){
        System.out.println(log);
        return log.insertOrUpdate();
    }

    public boolean deleteLog(int id){
        Log log = logDao.selectById(id);
        if (log != null){
            return log.deleteById();
        }
        return false;
    }

    public IPage<Log> listLog(PageDTO dto){
        Page<Log> page = new Page<>(dto.getPage(), dto.getLimit());
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("call_date");
        return logDao.selectPage(page, wrapper);
    }

    public int clearLog(){
        return logDao.delete(new QueryWrapper<>());
    }

    public int batchDeleteLog(List<Integer> ids){
        return logDao.deleteBatchIds(ids);
    }
}
