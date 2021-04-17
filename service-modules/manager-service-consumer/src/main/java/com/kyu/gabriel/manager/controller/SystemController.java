package com.kyu.gabriel.manager.controller;

import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.core.cache.RedisUtil;
import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.model.dto.PageDTO;
import com.kyu.gabriel.core.model.dto.ServiceDTO;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.result.Logger;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.manager.service.BookService;
import com.kyu.gabriel.manager.service.ManagerService;
import com.kyu.gabriel.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class SystemController {

    private final ManagerService managerService;
    private final UserService userService;
    private final BookService bookService;
    private final RedisUtil redisUtil;
    private final DiscoveryClient discoveryClient;
    private final ConfigMap configMap;


    @Autowired
    public SystemController(ManagerService managerService,
                            UserService userService,
                            BookService bookService,
                            DiscoveryClient discoveryClient,
                            ConfigMap configMap,
                            RedisTemplate<String, Object> redisTemplate){
        this.managerService = managerService;
        this.bookService = bookService;
        this.userService = userService;
        this.configMap = configMap;
        this.discoveryClient = discoveryClient;
        redisUtil = new RedisUtil(redisTemplate);
    }

    @Authentication(0)
    @RequestMapping("/log/list")
    public ResultMap<Map<String, Object>> listSysLogs(int page, int limit){
        PageDTO pageDTO = new PageDTO(page, limit);
        return managerService.listSystemLog(pageDTO);
    }

    @Authentication(0)
    @RequestMapping("/log/delete")
    public ResultMap<Integer> batchDeleteLog(@RequestBody int []ids){
        List<Integer> idList = Arrays.stream(ids).boxed().collect(Collectors.toList());
        return managerService.batchDeleteLog(idList);
    }

    @Authentication(0)
    @RequestMapping("/log/clear")
    @Logger("清空系统日志")
    public ResultMap<Integer> clearLog(){
        return managerService.clearLog();
    }

    @RequestMapping("/info")
    @Authentication(0)
    public ResultMap<Map<String, Object>> showIndexInfo(){
        Map<String, Object> result = new HashMap<>();
        int bookCount = bookService.getBookCount().getData();
        int userCount = userService.getUserCount().getData();
        int []registerCount = new int[7];
        for (int i = 1; i <= 7; i++) {
            String cacheKey = "register:count:" + i;
            if (redisUtil.existsKey(cacheKey)){
                registerCount[i - 1] = redisUtil.get(cacheKey, Integer.class);
            } else {
                registerCount[i - 1] = 0;
            }
        }
        result.put("bookCount", bookCount);
        result.put("userCount", userCount);
        result.put("registerCount", registerCount);
        return ResultMap.success(result);
    }

    @RequestMapping("/service")
    @Authentication(0)
    public ResultMap<Map<String, List<ServiceDTO>>> getCloudServices(){
        Map<String, List<ServiceDTO>> result = new HashMap<>();
        List<String> serviceNames = discoveryClient.getServices();
        serviceNames.forEach(service -> {
            List<ServiceDTO> serviceInstances = discoveryClient.getInstances(service)
                    .stream()
                    .map(s -> new ServiceDTO(s.getHost(), s.getPort(), s.getInstanceId(), s.getScheme(), s.isSecure()))
                    .collect(Collectors.toList());
            result.put(service, serviceInstances);
        });
        return ResultMap.success(result);
    }

    @RequestMapping("/config")
    @Authentication(0)
    public ResultMap<ConfigMap> getGlobalConfig(){
        configMap.loadConfig();
        return ResultMap.success(configMap);
    }

    @RequestMapping("/config/save")
    @Authentication(0)
    public ResultMap<Void> setGlobalConfig(HttpServletRequest request, @RequestBody Map<String, String> config){
        User user = (User) request.getAttribute("user");
        for (Map.Entry<String, String> entry: config.entrySet()){
            if (configMap.containsKey(entry.getKey())){
                configMap.replace(entry.getKey(), entry.getValue());
            }
        }
        configMap.save(user);
        return ResultMap.success();
    }
}
