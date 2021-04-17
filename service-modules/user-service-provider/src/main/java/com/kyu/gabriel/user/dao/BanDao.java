package com.kyu.gabriel.user.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kyu.gabriel.core.model.dto.ListBanDTO;
import com.kyu.gabriel.core.model.po.user.BanRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BanDao extends BaseMapper<BanRecord>{

    IPage<ListBanDTO> listBanRecord(IPage<BanRecord> page, @Param(Constants.WRAPPER) Wrapper<BanRecord> queryWrapper);
    int updateBatch(Collection<? extends BanRecord> records);
}
