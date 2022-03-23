package com.hyb.servicecenter.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("登录对象")
public class MobileVo {

    @ApiModelProperty("手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^((13[0-9])|(17[0-1,6-8])|(15[^4,\\\\D])|(18[0-9]))\\d{8}$",message = "手机号格式不正确")
    private String mobile;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    //TODO:短信验证码
}
