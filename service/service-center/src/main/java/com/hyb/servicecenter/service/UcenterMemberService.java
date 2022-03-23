package com.hyb.servicecenter.service;

import com.hyb.servicecenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyb.servicecenter.entity.vo.EmailVo;
import com.hyb.servicecenter.entity.vo.MobileVo;
import com.hyb.servicecenter.entity.vo.UserVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-03-04
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String toLogin(MobileVo mobileVo);

    String toLoginForEmail(EmailVo emailVo);

    boolean toRegister(UserVo userVo, String key);
}
