package com.kyu.gabriel.manager.controller;

import com.kyu.gabriel.core.cache.Cache;
import com.kyu.gabriel.core.cache.Empty;
import com.kyu.gabriel.core.model.po.manager.Answer;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/qa")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService){
        this.answerService = answerService;
    }

    @PutMapping("/")
    @Empty("answer")
    public ResultMap<Void> addQA(@RequestBody Answer answer){
        if (answerService.add(answer)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入失败");
    }

    @DeleteMapping("/{id}")
    @Empty("answer")
    public ResultMap<Void> deleteQA(@PathVariable int id){
        Answer answer = answerService.selectById(id);
        if (answer == null){
            return ResultMap.failed(3004, "数据不存在");
        } else if (answerService.delete(answer)){
            return ResultMap.success();
        } else {
            return ResultMap.failed(3001, "数据库写入失败");
        }
    }

    @PostMapping("/")
    @Empty("answer")
    public ResultMap<Void> updateQA(@RequestBody Answer answer){
        System.out.println(answer);
        if (answerService.update(answer)){
            return ResultMap.success();
        }
        return ResultMap.failed(3003, "数据无变化");
    }

    @GetMapping("/")
    @Cache("answer")
    public ResultMap<List<Answer>> listQA(){
        return ResultMap.success(answerService.listAll());
    }

    @GetMapping("/{id}")
    @Cache("answer:single")
    public ResultMap<Answer> getQA(@PathVariable int id){
        return ResultMap.success(answerService.selectById(id));
    }
}
