package com.kyu.gabriel.core.client;

import com.kyu.gabriel.core.model.po.book.Book;
import com.kyu.gabriel.core.model.vo.ListBookVO;
import com.kyu.gabriel.core.result.ResultMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Component
@FeignClient("BookServiceProvider")
public interface BookService {

    @PutMapping("/book")
    ResultMap<Void> addBook(@RequestBody Book book);

    @PostMapping("/book")
    ResultMap<Book> getBook(@RequestParam String uuid);

    @GetMapping("/book/{userUid}")
    ResultMap<List<Book>> getBooks(@PathVariable String userUid);

    @DeleteMapping("/book/{uid}")
    ResultMap<Void> deleteBook(@PathVariable String uid);

    @GetMapping("/count")
    ResultMap<Integer> getBookCount();

    @GetMapping("/count/{uuid}")
    ResultMap<Integer> getBookCount(@PathVariable String uuid);

    @PostMapping("/books")
    ResultMap<Map<String, Object>> listBooks(@RequestBody ListBookVO listBookVo);

    @DeleteMapping("/book")
    ResultMap<Integer> clearBooks(@RequestParam String userUid);

    @DeleteMapping("/books")
    ResultMap<Integer> deleteBookBatch(@RequestBody List<String> ids);

    @PostMapping(value = "/book/cover/{uid}")
    ResultMap<Void> changeCover(@PathVariable String uid, @RequestParam String coverName);

    @PutMapping("/book/update")
    ResultMap<Void> updateBookInfo(@RequestBody Book book);
}
