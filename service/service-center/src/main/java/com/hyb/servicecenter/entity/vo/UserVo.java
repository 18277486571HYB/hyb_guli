package com.hyb.servicecenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserVo {

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^((13[0-9])|(17[0-1,6-8])|(15[^4,\\\\D])|(18[0-9]))\\d{8}$",message = "手机号格式不正确")
    private String mobile;

//    TODO: 手机验证码

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;


    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!*&^()+-_~`|'.,:;{}]).{6,20})",message = "密码必须是6到20个字符串，其中至少包含一位数字，一个大写字母，一个小写字母和一个特殊符号（“@#$%!*&^()+-_~`|'.,:;{}”）")
    private String password;


    @ApiModelProperty(value = "确认密码")
    private String confirmPassword;


    @ApiModelProperty(value = "图形验证码")
    @NotBlank(message = "验证不能为空")
    private String code;


    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不能为空")
    @Length(min = 2,max = 15,message = "昵称长度必须是2-15")
    private String nickname;


}
