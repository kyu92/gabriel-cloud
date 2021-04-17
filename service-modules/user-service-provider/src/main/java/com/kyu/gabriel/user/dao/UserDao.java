package com.kyu.gabriel.user.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kyu.gabriel.core.model.dto.UserDTO;
import com.kyu.gabriel.core.model.po.user.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseMapper<User> {

    User selectUserByUniqueColumns(String column);
    IPage<UserDTO> listUserForAdmin(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);
}
