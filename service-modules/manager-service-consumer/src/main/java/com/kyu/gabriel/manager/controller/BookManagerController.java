package com.kyu.gabriel.manager.controller;

import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.core.cache.RedisUtil;
import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.file.MinioUtil;
import com.kyu.gabriel.core.model.dto.BookDTO;
import com.kyu.gabriel.core.model.po.book.Book;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.model.vo.ListBookVO;
import com.kyu.gabriel.core.result.CastUtil;
import com.kyu.gabriel.core.result.Logger;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.BookService;
import com.kyu.gabriel.manager.service.UserService;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookManagerController {

    private final BookService bookService;
    private final UserService userService;
    private final RedisUtil redisUtil;
    private final SimpleDateFormat format;
    private final MinioUtil minioUtil;
    private final Base64.Decoder decoder;

    @Autowired
    public BookManagerController(BookService bookService,
                                 UserService userService,
                                 ConfigMap configMap,
                                 RedisTemplate<String, Object> redisTemplate){
        this.bookService = bookService;
        this.userService = userService;
        this.redisUtil = new RedisUtil(redisTemplate);
        this.minioUtil = MinioUtil.getInstance(configMap);
        this.format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        decoder = Base64.getDecoder();
    }

    @RequestMapping("/list")
    @Authentication(0)
    public ResultMap<Map<String, Object>> listBookForAdmin(int page, int limit, String name, String type, String createDateStart, String createDateEnd){
        ListBookVO vo = new ListBookVO()
                .setPage(page)
                .setLimit(limit)
                .setName(name)
                .setType(type)
                .setCreateDateStart(createDateStart)
                .setCreateDateEnd(createDateEnd);
        Map<String, Object> res = bookService.listBooks(vo).getData();
        List<Map<String, Object>> books = CastUtil.castMapList(res.get("records"), String.class, Object.class);
        List<BookDTO> data = books.stream().map(book -> {
            String cacheKey = "tmp:owner:" + book.get("uuid");
            User user;
            if (redisUtil.existsKey(cacheKey)){
                user = redisUtil.get(cacheKey, User.class);
            } else {
                user = userService.queryUUIDExist((String) book.get("userUid")).getData();
                redisUtil.setEx(cacheKey, user, 10);
            }
            try {
                return new BookDTO()
                        .setUuid((String) book.get("uuid"))
                        .setName((String) book.get("name"))
                        .setCover((String) book.get("cover"))
                        .setType(book.get("suffix").toString())
                        .setCreateDate(format.parse((String) book.get("createDate")))
                        .setOwnerUid(user.getUuid())
                        .setOwnerName(user.getUsername());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new BookDTO();
        }).collect(Collectors.toList());
        res.put("records", data);
        return ResultMap.success(res);
    }

    @RequestMapping("/cover")
    @Authentication(0)
    public void showBookCover(String ownerUid, String coverName, HttpServletResponse response){
        InputStream is = minioUtil.download(ownerUid, coverName);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            IOUtils.copy(is, os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/delete")
    @Authentication(0)
    @Logger("删除书籍")
    public ResultMap<Void> deleteBook(String uuid){
        Book book = bookService.getBook(uuid).getData();
        try {
            minioUtil.remove(book.getUserUid(), book.getUuid() + book.getSuffix());
            minioUtil.remove(book.getUserUid(), book.getCover());
        } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
            e.printStackTrace();
        }
        return bookService.deleteBook(uuid);
    }

    @RequestMapping("/delete/batch")
    @Authentication(0)
    @Logger("批量删除书籍")
    public ResultMap<Integer> batchDeleteBooks(@RequestBody String []uuid){
        List<String> selected = new ArrayList<>();
        try {
            for (String each : uuid) {
                Book book = bookService.getBook(each).getData();
                minioUtil.remove(book.getUserUid(), book.getUuid() + book.getSuffix());
                minioUtil.remove(book.getUserUid(), book.getCover());
                selected.add(each);
            }
        } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
            e.printStackTrace();
        }
        return bookService.deleteBookBatch(selected);
    }

    @RequestMapping("/cover/{uuid}")
    @Authentication(0)
    @Logger(value = "修改书籍封面", exclude = "base64")
    public ResultMap<String> changeCover(@PathVariable String uuid, String base64){
        Book book = bookService.getBook(uuid).getData();
        try {
            if (book.getCover() != null){
                minioUtil.remove(book.getUserUid(), book.getCover());
            }
            String filename = System.currentTimeMillis() + ".jpg";
            byte []data = decoder.decode(base64);
            InputStream is = new ByteArrayInputStream(data);
            minioUtil.upload(book.getUserUid(), filename, is, "image/jpg");
            ResultMap<Void> res = bookService.changeCover(uuid, filename);
            if (res.isSuccessful()){
                return ResultMap.success(0, null, filename);
            }
            return ResultMap.failed(res.getStatusCode(), res.getMessage());
        } catch (IOException | InvalidKeyException | InvalidResponseException | InsufficientDataException | NoSuchAlgorithmException | ServerException | InternalException | XmlParserException | ErrorResponseException e) {
            e.printStackTrace();
        }
        return ResultMap.failed(2003, "文件操作时出现异常");
    }

    @Authentication(0)
    @Logger("下载书籍")
    @RequestMapping("/download/{uuid}")
    public ResultMap<String> downloadBook(@PathVariable String uuid){
        Book book = bookService.getBook(uuid).getData();
        if (book == null){
            return ResultMap.failed(3004, "未找到相关记录");
        }
        String url = minioUtil.download2URL(book.getUserUid(), book.getUuid() + book.getSuffix(), Method.GET, 300);
        return ResultMap.success(0, null, url);
    }
}
