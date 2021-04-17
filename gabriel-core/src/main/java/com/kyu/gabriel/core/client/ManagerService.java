package com.kyu.gabriel.core.client;

import com.kyu.gabriel.core.model.dto.PageDTO;
import com.kyu.gabriel.core.model.po.manager.Answer;
import com.kyu.gabriel.core.model.po.manager.Config;
import com.kyu.gabriel.core.model.po.manager.Feedback;
import com.kyu.gabriel.core.model.po.manager.Log;
import com.kyu.gabriel.core.model.vo.ListFeedbackVo;
import com.kyu.gabriel.core.result.ResultMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Component
@FeignClient("ManagerServiceProvider")
public interface ManagerService {

    @PutMapping("/log")
    ResultMap<Void> addLog(@RequestBody Log log);

    @DeleteMapping("/log/{id}")
    ResultMap<Void> deleteLog(@PathVariable int id);

    @PostMapping("/log/list")
    ResultMap<Map<String, Object>> listSystemLog(@RequestBody PageDTO dto);

    @DeleteMapping("/log/clear")
    ResultMap<Integer> clearLog();

    @DeleteMapping("/log")
    ResultMap<Integer> batchDeleteLog(@RequestBody List<Integer> ids);

    @GetMapping("/config/")
    ResultMap<List<Config>> getConfig();

    @GetMapping("/config/")
    ResultMap<Void> updateConfig(@RequestBody List<Config> configList);

    @GetMapping("/config/key/{configKey}")
    ResultMap<Config> getConfigByKey(@PathVariable String configKey);

    @GetMapping("/config/id/{configId}")
    ResultMap<Config> getConfigById(@PathVariable int configId);

    @PutMapping("/qa/")
    ResultMap<Void> addQA(@RequestBody Answer answer);

    @DeleteMapping("/qa/{id}")
    ResultMap<Void> deleteQA(@PathVariable int id);

    @PostMapping("/qa/")
    ResultMap<Void> updateQA(@RequestBody Answer answer);

    @GetMapping("/qa/")
    ResultMap<List<Answer>> listQA();

    @GetMapping("/qa/{id}")
    ResultMap<Answer> getQA(@PathVariable int id);

    @PutMapping("/feedback/")
    ResultMap<Void> putFeedback(@RequestBody Feedback feedback);

    @PostMapping("/feedback/")
    ResultMap<Map<String, Object>> listFeedback(@RequestBody ListFeedbackVo vo);

    @PostMapping("/feedback/status")
    ResultMap<Integer> setFeedbackStatus(@RequestBody List<Integer> ids, @RequestParam int status);

    @DeleteMapping("/feedback/")
    ResultMap<Integer> deleteFeedback(@RequestBody List<Integer> ids);
}
