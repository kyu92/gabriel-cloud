package com.kyu.gabriel.core.cache;

import com.kyu.gabriel.core.security.Encryptor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

@Aspect
@Component
public class CacheAspect {

    private final RedisUtil redisUtil;
    private final Base64.Encoder encoder;

    public CacheAspect(RedisTemplate<String, Object> redisTemplate){
        this.redisUtil = new RedisUtil(redisTemplate);
        this.encoder = Base64.getEncoder();
    }

    @Pointcut("@annotation(com.kyu.gabriel.core.cache.Cache)")
    public void add(){}

    @Pointcut("@annotation(com.kyu.gabriel.core.cache.Empty)")
    public void clear(){}

    private String genKeySuffix(HttpServletRequest request, JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry: parameterMap.entrySet()){
            sb.append(entry.getKey()).append("=").append(Arrays.toString(entry.getValue()));
        }
        sb.append("*").append(request.getRequestURI());
        Object [] args  = joinPoint.getArgs();
        StringBuilder sb2 = new StringBuilder();
        for (Object o: args){
            sb2.append("/*").append(o.toString()).append("*/");
        }
        String base64 = encoder.encodeToString(sb2.toString().getBytes(StandardCharsets.UTF_8));
        sb.append("*").append(base64);
        return Encryptor.to16MD5(sb.toString());
    }

    @Around("add()")
    public Object putCache(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Cache cache = targetMethod.getAnnotation(Cache.class);
        String suffix = genKeySuffix(request, joinPoint);
        String cacheKey = cache.value() + ":" + targetMethod.getName() + ":" + suffix;
        if (redisUtil.existsKey(cacheKey)){
            return redisUtil.get(cacheKey, Object.class);
        }
        Object obj = joinPoint.proceed();
        if (cache.time() < 0){
            redisUtil.set(cacheKey, obj);
            return obj;
        }
        long second = cache.timeUtil().toSeconds(cache.time());
        redisUtil.setEx(cacheKey, obj, second);
        return obj;
    }

    private void clearCache(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Empty empty = targetMethod.getAnnotation(Empty.class);
        for (String s: empty.value()){
            redisUtil.del(s + ":*");
        }
    }

    @Before("clear()")
    public void emptyBefore(JoinPoint joinPoint){
        clearCache(joinPoint);
    }

    @AfterReturning("clear()")
    public void emptyAfter(JoinPoint joinPoint){
        clearCache(joinPoint);
    }
}
