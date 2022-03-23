package com.hyb.servicecenter.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel("登录对象")
public class EmailVo {

    @ApiModelProperty("手机号")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    //TODO:邮箱验证码
}
