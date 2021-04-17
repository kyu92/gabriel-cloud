package com.kyu.gabriel.book.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kyu.gabriel.core.model.dto.BookDTO;
import com.kyu.gabriel.core.model.po.book.Book;
import com.kyu.gabriel.core.model.po.user.BanRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao extends BaseMapper<Book> {

    IPage<BookDTO> listBooksForAdmin(IPage<BanRecord> page, @Param(Constants.WRAPPER) Wrapper<BanRecord> queryWrapper);
}
