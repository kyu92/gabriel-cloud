package com.kyu.gabriel.core.client;

import com.kyu.gabriel.core.model.dto.BanDTO;
import com.kyu.gabriel.core.model.po.user.BanRecord;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.model.vo.ListBanRecordVO;
import com.kyu.gabriel.core.model.vo.ListUserVO;
import com.kyu.gabriel.core.result.ResultMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Component
@FeignClient("UserServiceProvider")
public interface UserService {

    @PostMapping("/query/username/{username}")
    ResultMap<User> queryUsernameExist(@PathVariable("username") String username);

    @PostMapping("/query/email/{email}")
    ResultMap<User> queryEmailExist(@PathVariable("email") String email);

    @PostMapping("/query/mobile/{mobile}")
    ResultMap<User> queryMobileExist(@PathVariable("mobile") String mobile);

    @PostMapping("/query/uuid/{uuid}")
    ResultMap<User> queryUUIDExist(@PathVariable("uuid") String uuid);

    @PostMapping("/query/column/{column}")
    ResultMap<User> queryByUniqueColumns(@PathVariable("column") String column);

    @PutMapping("/user")
    ResultMap<Void> addUser(@RequestBody User user);

    @PostMapping("/user")
    ResultMap<Void> updateUser(@RequestBody User user);

    @GetMapping("/user/ban/{uuid}")
    ResultMap<BanRecord> getUserBanInfo(@PathVariable("uuid") String uuid);

    @PostMapping("/users")
    ResultMap<Map<String, Object>> listUsers(@RequestBody ListUserVO listUserVo);

    @GetMapping("/count")
    ResultMap<Integer> getUserCount();

    @PostMapping("/reset/{uuid}")
    ResultMap<String> resetPassword(@PathVariable("uuid") String uuid);

    @PostMapping("/user/ban")
    ResultMap<Void> changeUserBanStatus(@RequestBody BanDTO banDTO);

    @DeleteMapping("/user")
    ResultMap<Void> deleteUser(String uuid);

    @PostMapping("/ban/list")
    ResultMap<Map<String, Object>> listBanRecord(@RequestBody ListBanRecordVO vo);

    @DeleteMapping("/ban/relieve/{id}")
    ResultMap<Void> relieveBan(@PathVariable("id") int id, @RequestBody User operator);

    @DeleteMapping("/ban/relieve/batch")
    ResultMap<Integer> relieveBanBatch(@RequestParam("ids") List<Integer> ids, @RequestBody User operator);

    @DeleteMapping("/ban/delete/{id}")
    ResultMap<Void> deleteBanRecord(@PathVariable("id") int id);

    @DeleteMapping("/ban/delete/batch")
    ResultMap<Integer> deleteBanRecordBatch(@RequestBody List<Integer> ids);
}
