package com.hyb.CommonUtil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Order(0)
public class RedisUtils implements InitializingBean {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    private static RedisTemplate<String,String> template;

    public static void setHash(String key,String key1,String value1,long timeout,TimeUnit timeUnit){
        //记录登录次数
        template.opsForHash().put(key,key1,value1);
//        System.out.println(redisTemplate.opsForHash().size("loginCount"));
        template.expire(key,timeout, timeUnit);
    }

    public static void set(String key,String value,long timeout,TimeUnit timeUnit){
        template.opsForValue().set(key,value,timeout,timeUnit);
    }

    public static String get(String key,String value,long timeout,TimeUnit timeUnit){
        return template.opsForValue().get(key);
    }



    public static long countHash(String hashKey){
        return template.opsForHash().size(hashKey);
    }

    public static boolean delHash(String hashKey){
        return Boolean.TRUE.equals(template.delete(hashKey));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        template=redisTemplate;
    }
}
