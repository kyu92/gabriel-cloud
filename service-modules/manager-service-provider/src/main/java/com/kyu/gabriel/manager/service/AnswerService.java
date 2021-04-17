package com.kyu.gabriel.manager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kyu.gabriel.core.model.po.manager.Answer;
import com.kyu.gabriel.manager.dao.AnswerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerDao answerDao;

    @Autowired
    public AnswerService(AnswerDao answerDao){
        this.answerDao = answerDao;
    }

    public boolean add(Answer answer){
        return answer.insert();
    }

    public Answer selectById(int id){
        return answerDao.selectById(id);
    }

    public boolean delete(Answer answer){
        return answer.deleteById();
    }

    public boolean update(Answer answer){
        return answer.updateById();
    }

    public List<Answer> listAll(){
        QueryWrapper<Answer> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_date");
        return answerDao.selectList(wrapper);
    }
}
