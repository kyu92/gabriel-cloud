package com.kyu.gabriel.manager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyu.gabriel.core.model.po.manager.Feedback;
import com.kyu.gabriel.core.string.StringUtils;
import com.kyu.gabriel.manager.dao.FeedbackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackDao feedbackDao;

    @Autowired
    public FeedbackService(FeedbackDao feedbackDao){
        this.feedbackDao = feedbackDao;
    }

    public boolean put(Feedback feedback){
        return feedback.insert();
    }

    public IPage<Feedback> list(String keyword, int page, int limit){
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)){
            wrapper.like("title", keyword).or().like("description", keyword);
        }
        Page<Feedback> pageInfo = new Page<>(page, limit);
        return feedbackDao.selectPage(pageInfo, wrapper);
    }

    public int setStatus(List<Integer> ids, int status){
        return feedbackDao.setStatus(ids, status);
    }

    public int delete(List<Integer> ids){
        return feedbackDao.remove(ids);
    }
}
