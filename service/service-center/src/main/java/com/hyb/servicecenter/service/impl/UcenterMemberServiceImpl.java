package com.hyb.servicecenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.CommonUtil.JwtUtils;
import com.hyb.CommonUtil.Md5Utils;
import com.hyb.CommonUtil.RedisUtils;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.servicecenter.entity.UcenterMember;
import com.hyb.servicecenter.entity.vo.EmailVo;
import com.hyb.servicecenter.entity.vo.MobileVo;
import com.hyb.servicecenter.entity.vo.UserVo;
import com.hyb.servicecenter.mapper.UcenterMemberMapper;
import com.hyb.servicecenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-03-04
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Value("${default.avatar}")
    private final String avatar=null;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public String toLogin(MobileVo mobileVo) {

        String mobile = mobileVo.getMobile();
        String password = mobileVo.getPassword();

        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();

        ucenterMemberQueryWrapper.eq("mobile",mobile);

        UcenterMember one = this.getOne(ucenterMemberQueryWrapper);
        if (one==null){
            throw new HybException(20001,"用户名不存在");
        }

        if (one.getIsDisabled()==1){
            throw new HybException(20001,"该用户已被禁用");
        }

        String toPassword = one.getPassword();

        if (!Md5Utils.md5(password).equals(toPassword)){
            throw new HybException(20001,"密码不正确");
        }

        //保存hash,并设置过期时间
        RedisUtils.setHash("loginCount",mobile+"","1",26, TimeUnit.HOURS);

        return JwtUtils.getJwtToken(one.getId(), one.getNickname());
    }

    @Override
    public String toLoginForEmail(EmailVo emailVo) {

        String email = emailVo.getEmail();
        String password = emailVo.getPassword();

        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();

        ucenterMemberQueryWrapper.eq("email",email);

        UcenterMember one = this.getOne(ucenterMemberQueryWrapper);
        if (one==null){
            throw new HybException(20001,"邮箱未注册账号");
        }

        if (one.getIsDisabled()==0){
            throw new HybException(20001,"该用户已被禁用");
        }

        String toPassword = one.getPassword();

        if (!Md5Utils.md5(toPassword).equals(password)){
            throw new HybException(20001,"密码不正确");
        }

        return JwtUtils.getJwtToken(one.getId(), one.getNickname());
    }

    @Override
    public boolean toRegister(UserVo userVo, String key) {

        if (!userVo.getPassword().equals(userVo.getConfirmPassword())){
            throw new HybException(20001,"两次密码不一致");
        }

        String checkCode = userVo.getCode();
        String s = redisTemplate.opsForValue().get(key);
        if(!checkCode.equals(s)){
            throw new HybException(20001,"验证不正确");
        }

        String mobile = userVo.getMobile();
        QueryWrapper<UcenterMember> ucenterMemberQueryWrapper = new QueryWrapper<>();
        ucenterMemberQueryWrapper.eq("mobile",mobile);
        int count = this.count(ucenterMemberQueryWrapper);
        if (count>0){
            throw new HybException(20001,"该手机号已被注册");
        }

        String nickname = userVo.getNickname();
        QueryWrapper<UcenterMember> memberQueryWrapper = new QueryWrapper<>();
        memberQueryWrapper.eq("nickname",nickname);
        int count1 = this.count(memberQueryWrapper);
        if (count1>0){
            throw new HybException(20001,"该用户已被注册");
        }

        String email = userVo.getEmail();
        QueryWrapper<UcenterMember> e = new QueryWrapper<>();
        e.eq("email",email);
        int count2 = this.count(e);
        if (count2>0){
            throw new HybException(20001,"该邮箱已被注册");
        }

        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(userVo,ucenterMember);

        ucenterMember.setAvatar(avatar);

        ucenterMember.setPassword(Md5Utils.md5(ucenterMember.getPassword()));

        RedisUtils.setHash("registerCount",mobile+"","1",26,TimeUnit.HOURS);

        return this.save(ucenterMember);
    }
}
