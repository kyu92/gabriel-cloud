package com.kyu.gabriel.manager.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kyu.gabriel.core.model.po.manager.Feedback;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface FeedbackDao extends BaseMapper<Feedback> {

    int setStatus(Collection<? extends Integer> ids, int status);
    int remove(Collection<? extends Integer> ids);
}
