package com.hyb.serviceedu.controller;

import com.hyb.CommonUtil.Msg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


@Api(tags="后台登录模拟")
@RestController
@RequestMapping(value = "/eduUser")
@CrossOrigin
public class EduLoginController {

    @ApiOperation("登录模拟")
    @PostMapping("/login")
    public Msg eduLogin(){
        return Msg.success().data("token","admin");
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Msg eduInfo(){
        String[] admins={"admin"};
        return Msg.success().data("roles",admins).data("name","admin").data("avatar","https://img1.baidu.com/it/u=4245241897,3920774166&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500");
    }
}
