package com.kyu.gabriel.core.cache;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisConnection getConnect(){
        assert this.redisTemplate.getConnectionFactory() != null;
        return this.redisTemplate.getConnectionFactory().getConnection();
    }

    public boolean set(String key, Object value){
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e){
            log.error(key, e);
            return false;
        }
    }

    public boolean setEx(String key, Object value, long second){
        try {
            redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
            return true;
        } catch (Exception e){
            log.error(key, e);
            return false;
        }
    }

    public <T> T get(String key, Class<T> clazz){
        try {
            Object o = redisTemplate.opsForValue().get(key);
            return clazz.cast(o);
        }  catch (Exception e){
            log.error(key, e);
            return null;
        }
    }

    public boolean expire(String key, long second){
        try {
            if (second > 0){
                redisTemplate.expire(key, second, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e){
            log.error(key, e);
            return false;
        }
    }

    public boolean expireAt(String key, @NotNull Date date){
        try {
            redisTemplate.expireAt(key, date);
            return true;
        } catch (Exception e){
            log.error(key, e);
            return false;
        }
    }

    public Long getExpire(String key){
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        }  catch (Exception e){
            log.error(key, e);
            return null;
        }
    }

    public boolean del(String patten){
        try {
            Set<String> keys = redisTemplate.keys(patten);
            if (keys != null){
                redisTemplate.delete(keys);
            }
            return true;
        } catch (Exception e){
            log.error("delete failed", e);
            return false;
        }
    }

    public boolean existsKey(String key){
        try {
            Boolean result = redisTemplate.hasKey(key);
            return result != null ? result : false;
        } catch (Exception e){
            log.error(key, e);
            return false;
        }
    }

    public int incr(String key){
        Long num = null;
        try {
            if (!existsKey(key)){
                num = 1L;
                set(key, num);
            } else {
                assert redisTemplate != null;
                num = redisTemplate.opsForValue().increment(key);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (num == null){
            num = 0L;
        }
        return Math.toIntExact(num);
    }
}
