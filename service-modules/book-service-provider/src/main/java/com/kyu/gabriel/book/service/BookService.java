package com.kyu.gabriel.book.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyu.gabriel.book.dao.BookDao;
import com.kyu.gabriel.core.model.po.book.Book;
import com.kyu.gabriel.core.model.vo.ListBookVO;
import com.kyu.gabriel.core.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookDao bookDao;

    @Autowired
    public BookService(BookDao bookDao){
        this.bookDao = bookDao;
    }

    public boolean add(Book book){
        return book.insertOrUpdate();
    }

    public Book getBook(String uuid){
        return bookDao.selectById(uuid);
    }

    public List<Book> getBooks(String userUid){
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uid", userUid).orderByDesc("last_read_date");
        return bookDao.selectList(wrapper);
    }

    public boolean delete(String uid){
        return new Book().deleteById(uid);
    }

    @Transactional
    public int clearBooks(String userUid){
        UpdateWrapper<Book> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_uid", userUid);
        return bookDao.delete(wrapper);
    }

    public int bookCount(){
        return bookDao.selectCount(new QueryWrapper<>());
    }

    public int bookCount(String uuid){
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.eq("user_uid", uuid);
        return bookDao.selectCount(wrapper);
    }

    public IPage<Book> listBooks(ListBookVO vo){
        Page<Book> page = new Page<>(vo.getPage(), vo.getLimit());
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(vo.getName())){
            wrapper.like("name", vo.getName());
        }
        if (!StringUtils.isEmpty(vo.getType())){
            wrapper.eq("suffix", vo.getType());
        }
        if (!StringUtils.isEmpty(vo.getCreateDateStart()) && !StringUtils.isEmpty(vo.getCreateDateEnd())){
            wrapper.between("create_date", vo.getCreateDateStart(), vo.getCreateDateEnd());
        }
        wrapper.orderByAsc("user_uid").orderByDesc("create_date");
        return bookDao.selectPage(page, wrapper);
    }

    public int deleteBatch(List<String> ids){
        return bookDao.deleteBatchIds(ids);
    }

    public boolean setCover(String uid, String coverName){
        Book book = bookDao.selectById(uid);
        if (book == null){
            return false;
        }
        book.setCover(coverName);
        return book.updateById();
    }

    public boolean update(Book book){
        return book.updateById();
    }
}
