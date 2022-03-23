package com.hyb.servicecenter.controller;


import com.hyb.CommonUtil.ErrorUtil;
import com.hyb.CommonUtil.JwtUtils;
import com.hyb.CommonUtil.Msg;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.servicecenter.entity.UcenterMember;
import com.hyb.servicecenter.entity.vo.EmailVo;
import com.hyb.servicecenter.entity.vo.MobileVo;
import com.hyb.servicecenter.entity.vo.UserVo;
import com.hyb.servicecenter.service.UcenterMemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/servicecenter/ucenterMember")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    UcenterMemberService ucenterMemberService;


    //TODO:手机验证码登录
    //TODO:邮箱验证码登录
    //TODO:注册需要手机验证码
    //TODO:注册需要邮箱验证码

    //手机号登录
    @ApiOperation("手机号登录")
    @PostMapping("/loginForMobile")
    public Msg loginForMobile(
            @ApiParam(value = "手机号和密码",name ="mobileVo" )@Valid @RequestBody MobileVo mobileVo, BindingResult result){
        Msg map = ErrorUtil.getError(result, "map", "数据格式不正确!");
        if (!map.getSuccess()){
            return map;
        }
        String token=null;
        try {
            token=ucenterMemberService.toLogin(mobileVo);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }

        return token==null?Msg.fail().message("返回token失败"):Msg.success().data("token",token);
    }

    @GetMapping("/loginForEmail")
    public Msg loginForEmail(
            @ApiParam(value = "邮箱和密码",name ="mobileVo" )@RequestBody @Valid EmailVo emailVo, BindingResult result){
        Msg map = ErrorUtil.getError(result, "map", "数据格式不正确!");
        if (!map.getSuccess()){
            return map;
        }
        String token=null;
        try {
            token=ucenterMemberService.toLoginForEmail(emailVo);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return token==null?Msg.fail().message("返回token失败"):Msg.success().data("token",token);
    }

    @ApiOperation("测试")
    @PostMapping("/testInsert")
    public Msg testInsert(@RequestBody UcenterMember ucenterMember){
        ucenterMemberService.save(ucenterMember);
        return Msg.success();
    }

    @ApiOperation("注册接口")
    @PostMapping("/register/{key}")
    public Msg register(@Valid @RequestBody UserVo userVo, BindingResult result,@PathVariable("key")String key){

        Msg map = ErrorUtil.getError(result, "map", "数据不规范!");
        if (!map.getSuccess()){
            return map;
        }
        boolean b=false;
        try{
            b=ucenterMemberService.toRegister(userVo,key);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return b?Msg.success():Msg.fail().message("注册失败");
    }

    @GetMapping("/get")
    public String get(){
        return "1111111111111";
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping(value = "/getUser")
    public Msg getUser(HttpServletRequest httpServletRequest){


        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(httpServletRequest);

        UcenterMember byId = ucenterMemberService.getById(memberIdByJwtToken);

        return byId==null?Msg.fail().message("请登录"):Msg.success().data("userInfo",byId);
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping(value = "/getUserInfo/{header}")
    public UcenterMember getUserInfo(@PathVariable("header")String header){


        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtTokenByHeader(header);

        return ucenterMemberService.getById(memberIdByJwtToken);
    }


}

