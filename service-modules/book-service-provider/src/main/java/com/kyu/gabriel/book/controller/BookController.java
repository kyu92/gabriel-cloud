package com.kyu.gabriel.book.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kyu.gabriel.book.service.BookService;
import com.kyu.gabriel.core.model.po.book.Book;
import com.kyu.gabriel.core.model.vo.ListBookVO;
import com.kyu.gabriel.core.result.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @PutMapping("/book")
    public ResultMap<Void> addBook(@RequestBody Book book){
        if (bookService.add(book)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, " 写入数据库时发生错误");
    }

    @PostMapping("/book")
    public ResultMap<Book> getBook(@RequestParam String uuid){
        return ResultMap.success(bookService.getBook(uuid));
    }

    @GetMapping("/count")
    public ResultMap<Integer> getBookCount(){
        return ResultMap.success(0, null, bookService.bookCount());
    }

    @GetMapping("/count/{uuid}")
    public ResultMap<Integer> getBookCount(@PathVariable String uuid){
        return ResultMap.success(0, null, bookService.bookCount(uuid));
    }

    @GetMapping("/book/{userUid}")
    public ResultMap<List<Book>> getBooks(@PathVariable String userUid){
        return ResultMap.success(bookService.getBooks(userUid));
    }

    @DeleteMapping("/book/{uid}")
    public ResultMap<Void> deleteBook(@PathVariable String uid){
        if (bookService.delete(uid)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "写入数据库时发生错误");
    }

    @DeleteMapping("/book")
    public ResultMap<Integer> clearBooks(@RequestParam String userUid){
        int count = bookService.clearBooks(userUid);
        return ResultMap.success(count);
    }

    @PostMapping("/books")
    public ResultMap<Map<String, Object>> listBooks(@RequestBody ListBookVO listBookVo){
        Map<String, Object> result = new HashMap<>();
        IPage<Book> books = bookService.listBooks(listBookVo);
        result.put("total", books.getTotal());
        result.put("records", books.getRecords());
        return ResultMap.success(result);
    }

    @DeleteMapping("/books")
    public ResultMap<Integer> deleteBookBatch(@RequestBody List<String> ids){
        int count = bookService.deleteBatch(ids);
        if (count > 0){
            return ResultMap.success(0, null, count);
        }
        return ResultMap.failed(3003, "数据无变化");
    }

    @PostMapping(value = "/book/cover/{uid}")
    public ResultMap<Void> changeCover(@PathVariable String uid, @RequestParam String coverName){
        if (bookService.setCover(uid, coverName)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入失败");
    }

    @PutMapping("/book/update")
    public ResultMap<Void> updateBookInfo(@RequestBody Book book){
        if (bookService.update(book)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入异常");
    }
}
