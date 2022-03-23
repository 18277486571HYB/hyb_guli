package com.hyb.servicecenter.controller;

import com.hyb.CommonUtil.Msg;
import com.hyb.servicecenter.entity.vo.SpecCaptchaVo;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.ReactiveStringCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 黄渝斌
 */
@Api(tags = "图形验证码")
@RestController
@RequestMapping("/servicecenter/check")
@CrossOrigin
public class CaptchaController {


    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Value("${default.codeTimeOut}")
    private Integer timeOut;



    @Deprecated
    @GetMapping(value = {"/captcha"})
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil.out(request, response);
    }


    @GetMapping("/captchaToRedis")
    public Msg captchaToRedis()throws Exception{
        SpecCaptcha specCaptcha = new SpecCaptcha(SpecCaptchaVo.width, SpecCaptchaVo.height, SpecCaptchaVo.length);
        String verCode = specCaptcha.text().toLowerCase();
        String key =System.currentTimeMillis()+"_"+ UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, verCode, timeOut, TimeUnit.MINUTES);

        Map<String,Object> map=new HashMap<>();
        map.put("key",key);
        map.put("src",specCaptcha.toBase64());
        return Msg.success().data("map",map);
    }

}
