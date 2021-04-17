package com.kyu.gabriel.manager.controller;

import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.file.MinioUtil;
import com.kyu.gabriel.core.model.dto.BanDTO;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.model.vo.ListUserVO;
import com.kyu.gabriel.core.result.Logger;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.BookService;
import com.kyu.gabriel.manager.service.UserService;
import io.minio.MinioClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserManagerController {

    private final UserService userService;
    private final BookService bookService;
    private final MinioUtil minioUtil;

    public UserManagerController(UserService userService,
                                 BookService bookService,
                                 ConfigMap configMap){
        this.userService = userService;
        this.bookService = bookService;
        this.minioUtil = MinioUtil.getInstance(configMap);
    }

    @RequestMapping("/list")
    @Authentication(0)
    public ResultMap<Map<String, Object>> listUserForAdmin(int page, int limit, String username, String email, String mobile,
                                                           String nick, String regDateStart, String regDateEnd, String loginDateStart,
                                                           String loginDateEnd, Boolean banned){
        ListUserVO vo = new ListUserVO()
                .setPage(page)
                .setLimit(limit)
                .setEmail(email)
                .setMobile(mobile)
                .setNick(nick)
                .setUsername(username)
                .setRegDateStart(regDateStart)
                .setRegDateEnd(regDateEnd)
                .setLoginDateStart(loginDateStart)
                .setLoginDateEnd(loginDateEnd)
                .setBanned(banned);
        return userService.listUsers(vo);
    }

    @RequestMapping("/reset")
    @Authentication(0)
    @Logger("重置密码")
    public ResultMap<String> resetUserPassword(String uuid){
        return userService.resetPassword(uuid);
    }

    @RequestMapping("/ban")
    @Authentication(0)
    @Logger("切换封禁状态")
    public ResultMap<Void> toggleUserBanStatus(HttpServletRequest request, String uuid, boolean status, String reason, long endDate){
        User operator = (User) request.getAttribute("user");
        BanDTO dto = new BanDTO()
                .setUuid(uuid)
                .setStatus(status)
                .setOperator(operator);
        if (status){
            dto.setReason(reason)
                    .setStartDate(new Date())
                    .setEndDate(new Date(endDate))
                    .setOperator(operator);
        }
        return userService.changeUserBanStatus(dto);
    }

    @RequestMapping("/delete")
    @GlobalTransactional(rollbackFor = Exception.class)
    @Authentication(0)
    @Logger("删除用户")
    public ResultMap<Void> deleteUserAndData(String uuid){
        if (!bookService.clearBooks(uuid).isSuccessful()){
            throw new RuntimeException("清除用户书籍失败");
        }
        if (!userService.deleteUser(uuid).isSuccessful()){
            throw new RuntimeException("删除用户数据失败");
        }
        if (!minioUtil.deleteAllFile(uuid)){
            throw new RuntimeException("清空用户文件失败");
        }
        if (!minioUtil.deleteBucket(uuid)){
            throw new RuntimeException("移除用户储存池失败");
        }
        return ResultMap.success();
    }
}
