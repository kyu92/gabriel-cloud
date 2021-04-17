package com.kyu.gabriel.manager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kyu.gabriel.core.model.po.manager.Feedback;
import com.kyu.gabriel.core.model.vo.ListFeedbackVo;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService){
        this.feedbackService = feedbackService;
    }

    @PutMapping("/")
    public ResultMap<Void> putFeedback(@RequestBody Feedback feedback){
        if (feedbackService.put(feedback)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入失败");
    }

    @PostMapping("/")
    public ResultMap<Map<String, Object>> listFeedback(@RequestBody ListFeedbackVo vo){
        Map<String, Object> result = new HashMap<>();
        IPage<Feedback> feedbacks = feedbackService.list(vo.getKeyword(), vo.getPage(), vo.getLimit());
        result.put("total", feedbacks.getTotal());
        result.put("records", feedbacks.getRecords());
        return ResultMap.success(result);
    }

    @PostMapping("/status")
    public ResultMap<Integer> setFeedbackStatus(@RequestBody List<Integer> ids, @RequestParam int status){
        int count = feedbackService.setStatus(ids, status);
        if (count > 0){
            return ResultMap.success(0, null, count);
        }
        return ResultMap.failed(3003, "数据无变化");
    }

    @DeleteMapping("/")
    public ResultMap<Integer> deleteFeedback(@RequestBody List<Integer> ids){
        int count = feedbackService.delete(ids);
        if (count > 0){
            return ResultMap.success(0, null, count);
        }
        return ResultMap.failed(3003, "数据无变化");
    }
}
