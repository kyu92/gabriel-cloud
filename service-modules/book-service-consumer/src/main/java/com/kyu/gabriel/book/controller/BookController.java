package com.kyu.gabriel.book.controller;

import com.kyu.gabriel.book.service.BookService;
import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.book.util.CoverUtil;
import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.file.MinioUtil;
import com.kyu.gabriel.core.model.po.book.Book;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.result.Logger;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.core.string.StringUtils;
import io.minio.errors.*;
import io.minio.http.Method;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
public class BookController {

    private final BookService bookService;
    private final MinioUtil minioUtil;
    private final List<String> permitSuffix = new ArrayList<>();
    private final String COVER_CONTENT_TYPE = "image/jpeg";

    @Autowired
    public BookController(BookService bookService,
                          ConfigMap configMap){
        this.bookService = bookService;
        this.minioUtil = MinioUtil.getInstance(configMap);
        permitSuffix.add(".epub");
        permitSuffix.add(".pdf");
        permitSuffix.add(".txt");
    }

    @Authentication
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @GlobalTransactional(rollbackFor = Exception.class)
    @Logger(value = "上传书籍")
    public ResultMap<Void> upload(HttpServletRequest request, MultipartFile file){
        if (file == null){
            return ResultMap.failed(2001, "文件不能为空");
        }
        String originalFileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        String fileName = StringUtils.getFileNameWithoutSuffix(originalFileName);
        String suffix = StringUtils.getFileSuffix(originalFileName);
        if (!permitSuffix.contains(suffix)){
            return ResultMap.failed(2002, "不支持该格式的文件");
        }
        String bookUid = UUID.randomUUID().toString();
        User user = (User) request.getAttribute("user");
        String basePath = "/" + user.getUuid() + "/book/";
        if (suffix.equalsIgnoreCase(".pdf")){
            String coverName = System.currentTimeMillis() + ".jpg";
            try {
                @Cleanup InputStream is = CoverUtil.genPdfCover(file.getInputStream());
//                fileService.save(coverName, basePath, is);
                minioUtil.upload(user.getUuid(), coverName, is, COVER_CONTENT_TYPE);
//                fileService.save(bookUid + suffix, basePath, file.getInputStream());
                minioUtil.upload(user.getUuid(), bookUid + suffix, file.getInputStream(), contentType);
                Book book = new Book(bookUid, user.getUuid(), fileName, coverName, suffix, new Date(), null, null);
                return bookService.addBook(book);
            } catch (IOException | XmlParserException | ServerException | NoSuchAlgorithmException | InsufficientDataException | InvalidKeyException | InvalidResponseException | ErrorResponseException | InternalException e) {
                e.printStackTrace();
            }
        } else if (suffix.equalsIgnoreCase(".epub")){
            try {
                @Cleanup InputStream is = CoverUtil.getEpubCover(file.getInputStream());
                String coverName = null;
                if (is != null){
                    coverName = System.currentTimeMillis() + ".jpg";
//                    fileService.save(coverName, basePath, is);
                    minioUtil.upload(user.getUuid(), coverName, is, COVER_CONTENT_TYPE);
                }
//                fileService.save(bookUid + suffix, basePath, file.getInputStream());
                minioUtil.upload(user.getUuid(), bookUid + suffix, file.getInputStream(), contentType);
                Book book = new Book(bookUid, user.getUuid(), fileName, coverName, suffix, new Date(), null, null);
                return bookService.addBook(book);
            } catch (IOException | XmlParserException | ServerException | NoSuchAlgorithmException | InsufficientDataException | InvalidKeyException | InvalidResponseException | ErrorResponseException | InternalException e) {
                e.printStackTrace();
            }
        } else {
            try {
//                fileService.save(bookUid + suffix, basePath, file.getInputStream());
                minioUtil.upload(user.getUuid(), bookUid + suffix, file.getInputStream(), contentType);
                Book book = new Book(bookUid, user.getUuid(), fileName, null, suffix, new Date(), null, null);
                return bookService.addBook(book);
            } catch (IOException | XmlParserException | ServerException | NoSuchAlgorithmException | InsufficientDataException | InvalidKeyException | InvalidResponseException | ErrorResponseException | InternalException e) {
                e.printStackTrace();
            }
        }
        return ResultMap.failed(2003, "保存文件时发生错误");
    }

    @Authentication
    @RequestMapping("/clear")
    @Logger("清空书籍")
    public ResultMap<Integer> clearBooks(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        List<Book> books = bookService.getBooks(user.getUuid()).getData();
        books.forEach(book -> {
//            fileService.delete(book.getUuid() + book.getSuffix(), book.getPath());
//            fileService.delete(book.getCover(), book.getPath());
            try {
                minioUtil.remove(book.getUserUid(), book.getUuid() + book.getSuffix());
                if (book.getCover() != null){
                    minioUtil.remove(book.getUserUid(), book.getCover());
                }
            } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
                e.printStackTrace();
            }
        });
        return bookService.clearBooks(user.getUuid());
    }

    @Authentication
    @RequestMapping("/delete/{uid}")
    @GlobalTransactional(rollbackFor = Exception.class)
    @Logger("删除书籍")
    public ResultMap<Void> deleteBook(HttpServletRequest request, @PathVariable String uid){
        User user = (User) request.getAttribute("user");
        Book book = bookService.getBook(uid).getData();
        if (bookService.deleteBook(uid).isSuccessful()){
//            if (fileService.delete(book.getUuid() + book.getSuffix(), book.getPath())){
//                fileService.delete(book.getCover(), book.getPath());
//                return ResultMap.success();
//            } else {
//                // 执行删除文件失败的逻辑
//                return ResultMap.success();
//            }
            try{
                minioUtil.remove(book.getUserUid(), book.getUuid() + book.getSuffix());
                if (book.getCover() != null){
                    minioUtil.remove(book.getUserUid(), book.getCover());
                }
                return ResultMap.success();
            } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidResponseException | ServerException | InsufficientDataException | ErrorResponseException | InternalException | XmlParserException | IOException e) {
                e.printStackTrace();
                return ResultMap.success();
            }
        } else {
            return ResultMap.failed(2001, "删除数据库记录时发生错误");
        }
    }

    @Authentication
    @RequestMapping("/book/{uid}")
    public void showBook(HttpServletRequest request, HttpServletResponse response, @PathVariable String uid){
        User user = (User) request.getAttribute("user");
        Book book = bookService.getBook(uid).getData();
        if (book == null || !book.getUserUid().equals(user.getUuid())){
            return;
        }
        book.setLastReadDate(new Date());
        try {
            OutputStream os = response.getOutputStream();
            bookService.updateBookInfo(book);
//            fileService.getFile(book.getUuid() + book.getSuffix(), book.getPath(), os);
            InputStream is = minioUtil.download(book.getUserUid(), book.getUuid() + book.getSuffix());
            IOUtils.copy(is, os);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + (book.getName() + book.getSuffix()));
            os.flush();
            os.close();
        } catch (IOException | XmlParserException | ServerException | NoSuchAlgorithmException | InsufficientDataException | InvalidKeyException | InvalidResponseException | ErrorResponseException | InternalException e) {
            e.printStackTrace();
        }
    }

    @Authentication
    @RequestMapping("/book/url/{uid}")
    @Logger("下载书籍")
    public ResultMap<String> getBookUrl(HttpServletRequest request, @PathVariable String uid){
        User user = (User) request.getAttribute("user");
        Book book = bookService.getBook(uid).getData();
        if (book == null || !book.getUserUid().equals(user.getUuid())){
            return ResultMap.failed(9004, "你无权请求该资源");
        }
        String url;
        try {
            url = minioUtil.download2URL(book.getUserUid(), book.getUuid() + book.getSuffix(), Method.GET, 60);
            return ResultMap.success(0, null, url);
        } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
            e.printStackTrace();
            return ResultMap.failed(2003, e.getMessage());
        }
    }

    @RequestMapping("/cover/{uid}")
    public void showBookCover(HttpServletResponse response, @PathVariable String uid){
        Book book = bookService.getBook(uid).getData();
        if (book == null || StringUtils.isEmpty(book.getCover())){
            return;
        }
        try {
            OutputStream os = response.getOutputStream();
//            fileService.getFile(book.getCover(), book.getPath(), os);
            InputStream is = minioUtil.download(book.getUserUid(), book.getCover());
            IOUtils.copy(is, os);
            response.setContentType(COVER_CONTENT_TYPE);
            response.setHeader("Content-Disposition", "attachment;filename=" + (book.getName() + ".jpg"));
            os.flush();
            os.close();
        } catch (IOException | XmlParserException | ServerException | NoSuchAlgorithmException | InsufficientDataException | InvalidKeyException | InvalidResponseException | ErrorResponseException | InternalException e) {
            e.printStackTrace();
        }
    }

    @Authentication
    @RequestMapping("/books")
    public ResultMap<List<Map<String, Object>>> listBooks(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        List<Book> books = bookService.getBooks(user.getUuid()).getData();
        List<Map<String, Object>> result = new ArrayList<>();
        books.forEach(book -> {
            Map<String, Object> data = new HashMap<>();
            data.put("uuid", book.getUuid());
            data.put("name", book.getName());
            data.put("type", book.getSuffix().replace(".", ""));
            data.put("createDate", book.getCreateDate());
            data.put("lastReadDate", book.getLastReadDate());
            result.add(data);
        });
        return ResultMap.success(result);
    }

    @Authentication
    @RequestMapping("/progress/save")
    public ResultMap<Void> saveProcess(HttpServletRequest request, String uuid, String progressData){
        User user = (User) request.getAttribute("user");
        Book book = bookService.getBook(uuid).getData();
        System.out.println(progressData);
        if (book.getUserUid().equals(user.getUuid())){
            book.setProgress(progressData);
            return bookService.updateBookInfo(book);
        }
        return ResultMap.failed(1005, "你无权操作这本书籍");
    }

    @Authentication
    @RequestMapping("/info")
    public ResultMap<Book> getBookInfo(String uuid){
        return bookService.getBook(uuid);
    }
}
