package com.kyu.gabriel.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kyu.gabriel.core.cache.Cache;
import com.kyu.gabriel.core.cache.Empty;
import com.kyu.gabriel.core.model.dto.PageDTO;
import com.kyu.gabriel.core.model.po.manager.Log;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService){
        this.logService = logService;
    }

    @PutMapping("/log")
    @Empty("log")
    public ResultMap<Void> addLog(@RequestBody Log log){
        if (logService.addLog(log)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入失败");
    }

    @DeleteMapping("/log/{id}")
    @Empty("log")
    public ResultMap<Void> deleteLog(@PathVariable int id){
        if (logService.deleteLog(id)){
            return ResultMap.success();
        }
        return ResultMap.failed(3003, "数据无变化");
    }

    @PostMapping("/log/list")
    @Cache(value = "log", time = 5)
    public ResultMap<Map<String, Object>> listSystemLog(@RequestBody PageDTO dto){
        Map<String, Object> result = new HashMap<>();
        IPage<Log> logs = logService.listLog(dto);
        result.put("total", logs.getTotal());
        result.put("records", logs.getRecords());
        return ResultMap.success(result);
    }

    @DeleteMapping("/log/clear")
    @Empty("log")
    public ResultMap<Integer> clearLog(){
        int count = logService.clearLog();
        if (count > 0){
            return ResultMap.success(0, null, count);
        }
        return ResultMap.failed(3003, "数据无变化");
    }

    @DeleteMapping("/log")
    @Empty("log")
    public ResultMap<Integer> batchDeleteLog(@RequestBody List<Integer> ids){
        System.out.println();
        int count = logService.batchDeleteLog(ids);
        if (count > 0){
            return ResultMap.success(0, null, count);
        }
        return ResultMap.failed(3003, "数据无变化");
    }
}
