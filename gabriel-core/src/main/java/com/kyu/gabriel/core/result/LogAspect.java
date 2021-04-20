package com.kyu.gabriel.core.result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyu.gabriel.core.client.ManagerService;
import com.kyu.gabriel.core.config.ConfigMap;
import com.kyu.gabriel.core.model.dto.TencentLocDTO;
import com.kyu.gabriel.core.model.po.manager.Log;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.core.security.IPUtil;
import com.kyu.gabriel.core.string.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j(topic = "LogAspect")
public class LogAspect {

    private final ApplicationContext applicationContext;
    private Log logObj;
    private final ObjectMapper mapper;
    private boolean hasBean = false;
    private ManagerService managerService;
    private final ConfigMap configMap;
    private Logger logger;

    @Autowired
    public LogAspect(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
        if (Arrays.stream(applicationContext.getBeanDefinitionNames()).anyMatch(s -> s.contains("ManagerService"))) {
            configMap = applicationContext.getBean(ConfigMap.class);
        } else {
            configMap = null;
        }
        mapper = new ObjectMapper();
    }

    private String getParams(HttpServletRequest request, Logger logger) throws JsonProcessingException {
        List<String> exclude = Arrays.stream(logger.exclude()).collect(Collectors.toList());
        String contentType = request.getContentType();
        log.info("Content-Type: " + contentType);
        if (contentType.contains("form-data") || contentType.contains("application/x-www-form-urlencoded")){
            Map<String, Object> params = new HashMap<>();
            for (Map.Entry<String, String[]> entry: request.getParameterMap().entrySet()){
                if (!exclude.contains(entry.getKey())){
                    String []values = entry.getValue();
                    if (values.length > 1){
                        params.put(entry.getKey(), values);
                    } else if (values.length == 0){
                        params.put(entry.getKey(), null);
                    } else {
                        params.put(entry.getKey(), values[0]);
                    }
                }
            }
            return mapper.writeValueAsString(params);
        } else if (contentType.equalsIgnoreCase("application/json")) {
            try {
                InputStream is = request.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "无法解析的参数格式";
    }

    private String getParams(Method method, JoinPoint joinPoint) throws JsonProcessingException {
        Object []args = joinPoint.getArgs();
        List<String> excludeField = Arrays.asList(logger.exclude().clone());
        log.debug("excludeFields: " + excludeField);
        List<Class<?>> excludeClass = Arrays.asList(logger.excludeClass().clone());
        log.debug("excludeClass: " + excludeClass);
        Map<String, Object> params = new HashMap<>();
        Parameter []parameters = method.getParameters();
        for (int i = 0, l = parameters.length; i < l; i++) {
            String parameterName = parameters[i].getName();
            Class<?> parameterClass = parameters[i].getType();
            if (!excludeField.contains(parameterName) && !excludeClass.contains(parameterClass)){
                params.put(parameterName, args[i]);
            } else {
                log.debug("参数: '" + parameterName + "'已被排除log统计");
            }
        }
        log.debug("params: " + params);
        return mapper.writeValueAsString(params);
    }

    @Pointcut("@annotation(com.kyu.gabriel.core.result.Logger)")
    public void logger(){}

    @Before("logger()")
    public void before(JoinPoint joinPoint) throws IOException {
        List<String> beans = Arrays.stream(applicationContext.getBeanDefinitionNames()).collect(Collectors.toList());
        hasBean = beans.stream().anyMatch(s -> s.contains("ManagerService"));
        log.debug("hasManagerServiceBean: " + hasBean);
        if (!hasBean){
            log.error("当前微服务模块不存在ManagerService Bean，请使用ManagerService作为接口类名，并继承com.kyu.gabriel.core.client.ManagerService");
            return;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        logger = targetMethod.getAnnotation(Logger.class);
        log.info("title: " + logger.value());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        User user = (User) request.getAttribute("user");
        String ip = IPUtil.getIpAddr(request);
        log.debug("IP: " + ip);
        String params = getParams(targetMethod, joinPoint);
        log.debug("params: " + params);
        logObj = new Log();
        logObj.setTitle(logger.value())
                .setMethod(targetMethod.getName())
                .setFullName(targetMethod.getDeclaringClass().getName())
                .setUserAgent(request.getHeader("User-Agent"))
                .setCallDate(new Date())
                .setParams(params)
                .setPath(request.getRequestURI())
                .setCallerIp(ip);
        if (user != null){
            logObj.setCallerUuid(user.getUuid());
        }
    }

    /**
     * 设置调用者的位置信息
     * 传入Log对象的引用，该逻辑会从Log中取出IP，并分析地址
     * 完成后将会直接将地址信息写入Log对象
     * 该方法必须在before之后调用
     * @param logObj log对象
     */
    private void setCallerLocInfo(Log logObj){
        if (!Boolean.parseBoolean(configMap.get("queryLocation"))){
            return;
        }
        final String ip = logObj.getCallerIp();
        if (!StringUtils.isEmpty(ip)){
            try {
                TencentLocDTO dto = IPUtil.getIPLocation(ip, configMap.get("webServiceKey"), configMap.get("webServiceSK"));
                if(dto != null){
                    if (dto.getResult() == null){
                        log.warn(dto.getMessage());
                        return;
                    }
                    logObj.setCallerLon(dto.getResult().getLocation().getLng());
                    logObj.setCallerLat(dto.getResult().getLocation().getLat());
                    TencentLocDTO.Result.ADInfo info = dto.getResult().getAd_info();
                    logObj.setCallerLocation(info.getNation() + "-" + info.getProvince() + "-" + info.getCity());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterReturning(value = "logger()", returning = "ret")
    public void after(Object ret){
        setCallerLocInfo(logObj);
        ResultMap<?> res = null;
        try {
            if (ret instanceof ResultMap){
                res = ((ResultMap<?>) ret).clone();
                if (logger.excludeResponseData()){
                    res.setData(null);
                }
                logObj.setSuccess(res.isSuccessful());
            }
            String resStr = mapper.writeValueAsString(res);
            logObj.setResponse(resStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logObj.setException(e.getMessage())
                    .setSuccess(true);
        } catch (CloneNotSupportedException e) {
            log.error("响应对象克隆失败", e);
        }
        if (hasBean){
            log.debug("log对象数据: " + logObj);
            managerService = applicationContext.getBean(ManagerService.class);
            managerService.addLog(logObj);
        }
    }

    @AfterThrowing(value = "logger()", throwing = "throwing")
    public void throwing(Throwable throwing){
        setCallerLocInfo(logObj);
        logObj.setException(throwing.getMessage())
                .setSuccess(false);
        if (hasBean){
            managerService = applicationContext.getBean(ManagerService.class);
            managerService.addLog(logObj);
        }
    }
}
