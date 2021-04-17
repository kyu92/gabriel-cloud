package com.kyu.gabriel.book.controller;

import com.kyu.gabriel.book.service.BookService;
import com.kyu.gabriel.core.model.po.book.Book;
import com.kyu.gabriel.core.result.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    private BookService bookService;

    @PutMapping("/test/book")
    public ResultMap<Object> addBook(){
        Book book = new Book(UUID.randomUUID().toString(), UUID.randomUUID().toString(), ".pdf", new Date());
        if (bookService.add(book)){
            return ResultMap.success();
        }
        return ResultMap.failed(200);
    }
}
