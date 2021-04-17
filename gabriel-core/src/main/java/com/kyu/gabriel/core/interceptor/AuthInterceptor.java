package com.kyu.gabriel.core.interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyu.gabriel.core.auth.Authentication;
import com.kyu.gabriel.core.client.UserService;
import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.result.JWTUtil;
import com.kyu.gabriel.core.result.ResultMap;
import com.kyu.gabriel.core.string.StringUtils;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;
    private final ConfigMap configMap;

    public AuthInterceptor(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
        this.objectMapper = new ObjectMapper();
        configMap = applicationContext.getBean(ConfigMap.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String message;
        try {
            Method target = ((HandlerMethod) handler).getMethod();
            Authentication authentication = target.getAnnotation(Authentication.class);
            String token = request.getHeader("Authorization");
            if (authentication == null){
                return true;
            }
            message = "用户未授权";
            if (!StringUtils.isEmpty(token)){
                DecodedJWT jwt = JWTUtil.verifyJWT(token, configMap.get("jwtSecret"));
                String uuid = JWTUtil.getClaim(jwt, "uuid", String.class);
                log.info("用户校验: " + uuid);
                UserService userService = applicationContext.getBean(UserService.class);
                ResultMap<User> result = userService.queryUUIDExist(uuid);
                User user = result.getData();
                if (user.getPermission() > authentication.value()){
                    message = "权限不足!";
                } else {
                    log.info("校验通过: " + user);
                    request.setAttribute("user", user);
                    return true;
                }
            }
        } catch (ClassCastException e){
            log.error("接收到未定义的方法" + request.getRequestURI());
            return false;
        } catch (JWTVerificationException e){
            message = "token校验未通过, 或授权已过期";
        }
        response.setHeader("Content-type", "application/json;charset=utf-8;");
        response.setStatus(403);
        PrintWriter writer = response.getWriter();
        writer.print(objectMapper.writeValueAsString(ResultMap.failed(9003, message)));
        return false;
    }
}
