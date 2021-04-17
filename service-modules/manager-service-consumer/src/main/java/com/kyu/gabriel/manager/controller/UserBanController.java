package com.kyu.gabriel.manager.controller;

import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.model.vo.ListBanRecordVO;
import com.kyu.gabriel.core.result.Logger;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ban")
public class UserBanController {

    private final UserService userService;

    @Autowired
    public UserBanController(UserService userService){
        this.userService = userService;
    }

    @Authentication(0)
    @RequestMapping("/list")
    public ResultMap<Map<String, Object>> listBanRecord(int page, int limit, String username, String uuid, String relieveDateStart,
                                                        String relieveDateEnd, String reason, String operator, Boolean deleteFlag){
        ListBanRecordVO vo = new ListBanRecordVO()
                .setPage(page)
                .setLimit(limit)
                .setUsername(username)
                .setUuid(uuid)
                .setOperator(operator)
                .setReason(reason)
                .setDeleteFlag(deleteFlag)
                .setRelieveDateEnd(relieveDateEnd)
                .setRelieveDateStart(relieveDateStart);
        return userService.listBanRecord(vo);
    }

    @Authentication(0)
    @RequestMapping("/relieve/{id}")
    @Logger("撤销封禁记录")
    public ResultMap<Void> relieveBan(HttpServletRequest request, @PathVariable int id){
        User operator = (User) request.getAttribute("user");
        System.out.println(operator);
        return userService.relieveBan(id, operator);
    }

    @Authentication(0)
    @RequestMapping("/relieve/batch")
    @Logger("批量撤销封禁记录")
    public ResultMap<Void> relieveBanBatch(HttpServletRequest request, @RequestBody int[] ids){
        User operator = (User) request.getAttribute("user");
        ResultMap<Integer> res = userService.relieveBanBatch(Arrays.stream(ids).boxed().collect(Collectors.toList()), operator);
        int line = res.getData();
        if (line > 0){
            return ResultMap.success();
        }
        return ResultMap.failed(3003, "数据库信息没有发生变化");
    }

    @Authentication(0)
    @RequestMapping("/delete/{id}")
    @Logger("删除封禁记录")
    public ResultMap<Void> deleteBanRecord(@PathVariable int id){
        return userService.deleteBanRecord(id);
    }

    @Authentication(0)
    @RequestMapping("/delete/batch")
    @Logger("批量删除封禁记录")
    public ResultMap<Void> deleteBanBatch(@RequestBody int[] ids){
        System.out.println(Arrays.toString(ids));
        int line = userService.deleteBanRecordBatch(Arrays.stream(ids).boxed().collect(Collectors.toList())).getData();
        if (line > 0){
            return ResultMap.success();
        }
        return ResultMap.failed(3003, "数据库信息没有发生变化");
    }
}
