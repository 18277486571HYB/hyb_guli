package com.hyb.gateway.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils{

    @Autowired
    RedisTemplate<String,String> template;


    public  void setHash(String key,String key1,String value1,long timeout,TimeUnit timeUnit){
        //记录登录次数
        template.opsForHash().put(key,key1,value1);
//        System.out.println(redisTemplate.opsForHash().size("loginCount"));
        template.expire(key,timeout, timeUnit);
    }

    public  void set(String key,String value,long timeout,TimeUnit timeUnit){
        template.opsForValue().set(key,value,timeout,timeUnit);
    }

    public  String get(String key){
        return template.opsForValue().get(key);
    }

    public Set<String> getSet(String key){
        return template.opsForSet().members(key);
    }



    public long countHash(String hashKey){
        return template.opsForHash().size(hashKey);
    }

    public boolean delHash(String hashKey){
        return Boolean.TRUE.equals(template.delete(hashKey));
    }

}
