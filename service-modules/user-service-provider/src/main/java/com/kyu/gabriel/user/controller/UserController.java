package com.kyu.gabriel.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kyu.gabriel.core.cache.Cache;
import com.kyu.gabriel.core.cache.Empty;
import com.kyu.gabriel.core.cache.RedisUtil;
import com.kyu.gabriel.core.model.dto.UserDTO;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.model.vo.ListUserVO;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.core.string.StringUtils;
import com.kyu.gabriel.user.service.UserService;
import com.kyu.gabriel.core.model.dto.BanDTO;
import com.kyu.gabriel.core.model.dto.ListBanDTO;
import com.kyu.gabriel.core.model.po.user.BanRecord;
import com.kyu.gabriel.core.model.vo.ListBanRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RefreshScope
@Slf4j
public class UserController {

    private final UserService userService;
    private final RedisUtil redisUtil;

    @Autowired
    public UserController(UserService userService, RedisTemplate<String, Object> redisTemplate){
        this.userService = userService;
        this.redisUtil = new RedisUtil(redisTemplate);
    }

    @PostMapping("/query/username/{username}")
    @Cache("user:query")
    public ResultMap<User> queryUsernameExist(@PathVariable String username){
        User user = userService.getUserByUsername(username);
        return ResultMap.success(user);
    }

    @PostMapping("/query/email/{email}")
    @Cache("user:query")
    public ResultMap<User> queryEmailExist(@PathVariable String email){
        User user = userService.getUserEmail(email);
        return ResultMap.success(user);
    }

    @PostMapping("/query/mobile/{mobile}")
    @Cache("user:query")
    public ResultMap<User> queryMobileExist(@PathVariable String mobile){
        User user = userService.getUserByMobilePhone(mobile);
        return ResultMap.success(user);
    }

    @PostMapping("/query/uuid/{uuid}")
    @Cache("user:query")
    public ResultMap<User> queryUUIDExist(@PathVariable String uuid){
        User user = userService.getUserByUUID(uuid);
        return ResultMap.success(user);
    }

    @PostMapping("/query/column/{column}")
    @Cache("user:query")
    public ResultMap<User> queryByUniqueColumns(@PathVariable String column){
        User user = userService.getUserByUniqueColumns(column);
        return ResultMap.success(user);
    }

    @PutMapping("/user")
    @Empty({"user:list", "user:count"})
    public ResultMap<Void> addUser(@RequestBody User user){
        if (userService.add(user)){
            Calendar today = Calendar.getInstance();
            today.setFirstDayOfWeek(Calendar.MONDAY);
            int dayOfWeek = today.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek == 0){
                dayOfWeek = 7;
            }
            String cacheKey = "register:count:" + dayOfWeek;
            today.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            today.add(Calendar.DATE, 1);
            today.set(Calendar.MILLISECOND, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.HOUR, 0);
            redisUtil.incr(cacheKey);
            redisUtil.expireAt(cacheKey, today.getTime());
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入失败");
    }

    @PostMapping("/user")
    @Empty({"user:query", "user:list", "user:count"})
    public ResultMap<Void> updateUser(@RequestBody User user){
        if (userService.update(user)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入失败");
    }

    @DeleteMapping("/user")
    @Empty({"user:query", "user:list", "user:count"})
    public ResultMap<Void> deleteUser(@RequestBody String uuid){
        User user = userService.getUserByUUID(uuid);
        if (userService.delete(user)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库写入失败");
    }

    @PostMapping("/users")
    @Cache("user:list")
    public ResultMap<Map<String, Object>> listUsers(@RequestBody ListUserVO listUserVo){
        Map<String, Object> result = new HashMap<>();
        IPage<UserDTO> users = userService.selectUsers(listUserVo);
        result.put("records", users.getRecords());
        result.put("total", users.getTotal());
        return ResultMap.success(result);
    }

    @GetMapping("/count")
    @Cache("user:count")
    public ResultMap<Integer> getUserCount(){
        return ResultMap.success(0, userService.userCount());
    }

    @PostMapping("/reset/{uuid}")
    public ResultMap<String> resetPassword(@PathVariable String uuid){
        String password = userService.resetUserPassword(uuid);
        if (StringUtils.isEmpty(password)){
            return ResultMap.failed(3001, "用户不存在或者写入数据异常", null);
        }
        return ResultMap.success(0, null, password);
    }

    @PostMapping("/user/ban")
    @Empty("ban:list")
    public ResultMap<Void> changeUserBanStatus(@RequestBody BanDTO banDTO){
        User user = userService.getUserByUUID(banDTO.getUuid());
        log.info("封禁用户: " + banDTO.getUuid() + ", 理由: " + banDTO.getReason());
        if (user.getPermission() == 0){
            return ResultMap.failed(1005, "你不能封禁管理员用户");
        }
        if (userService.setUserStatus(banDTO)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "数据库执行过程出现异常");
    }

    @GetMapping("/user/ban/{uuid}")
    @Cache("user:ban")
    public ResultMap<BanRecord> getUserBanInfo(@PathVariable String uuid){
        BanRecord record = userService.getBanRecordByUUid(uuid);
        return ResultMap.success(record);
    }

    @PostMapping("/ban/list")
    @Cache("ban:list")
    public ResultMap<Map<String, Object>> listBanRecord(@RequestBody ListBanRecordVO vo){
        Map<String, Object> result = new HashMap<>();
        IPage<ListBanDTO> banRecords = userService.listBanRecords(vo);
        result.put("records", banRecords.getRecords());
        result.put("total", banRecords.getTotal());
        return ResultMap.success(result);
    }

    @DeleteMapping("/ban/relieve/{id}")
    @Empty({"ban:list", "user:ban"})
    public ResultMap<Void> relieveBan(@PathVariable int id, @RequestBody User operator){
        if (userService.relieve(id, operator)){
            return ResultMap.success();
        }
        return ResultMap.failed(3001, "更新数据时出现异常");
    }

    @DeleteMapping("/ban/relieve/batch")
    @Empty({"ban:list", "user:ban"})
    public ResultMap<Integer> relieveBanBatch(@RequestParam List<Integer> ids, @RequestBody User operator){
        return ResultMap.success(0, null, userService.relieveBatch(ids, operator));
    }

    @DeleteMapping("/ban/delete/{id}")
    @Empty({"ban:list", "user:ban"})
    public ResultMap<Void> deleteBanRecord(@PathVariable int id){
        return userService.deleteBanRecord(id) ? ResultMap.success() : ResultMap.failed(3001, "删除数据时出现异常");
    }

    @DeleteMapping("/ban/delete/batch")
    @Empty({"ban:list", "user:ban"})
    public ResultMap<Integer> deleteBanRecordBatch(@RequestBody List<Integer> ids){
        return ResultMap.success(0, null, userService.deleteBanRecordBatch(ids));
    }
}
