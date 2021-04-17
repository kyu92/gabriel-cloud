package com.kyu.gabriel.user.controller;

import com.kyu.gabriel.core.cache.Cache;
import com.kyu.gabriel.core.cache.Empty;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

//    @PutMapping("/user")
//    public ResultMap<Object> addUser(){
//        String username = StringGenerator.randomStr(6);
//        if (userService.register(username, "123456", null, null)){
//            return ResultMap.success();
//        }
//        return ResultMap.failed(200);
//    }

    @GetMapping("/cookie")
    public void cookieTest(HttpServletResponse response){
        response.addCookie(new Cookie("test", "test"));
    }

    @GetMapping("/cache")
    @Cache(value = "test", time = 60)
    public ResultMap<String> cacheTest(){
        return ResultMap.success(0, null, "Hello Cache");
    }

    @GetMapping("/clear")
    @Empty("test")
    public ResultMap<Void> clearCache(){
        return ResultMap.success();
    }
}
