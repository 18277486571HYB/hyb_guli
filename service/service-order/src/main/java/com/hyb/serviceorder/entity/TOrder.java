package com.hyb.serviceorder.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author hyb
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TOrder对象", description="订单")
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "订单号")
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "课程id")
    @NotBlank(message = "课程号不能为空")
    private String courseId;

    @ApiModelProperty(value = "课程名称")
    @NotBlank(message = "课程名称不能为空")
    private String courseTitle;

    @ApiModelProperty(value = "课程封面")
    private String courseCover;

    @ApiModelProperty(value = "讲师名称")
    @NotBlank(message = "讲师不能为空")
    private String teacherName;

    @ApiModelProperty(value = "会员id")
    @NotBlank(message = "用户id不能为空")
    private String memberId;

    @ApiModelProperty(value = "会员昵称")
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;

    @ApiModelProperty(value = "会员手机")
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    @ApiModelProperty(value = "订单金额（分）")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "支付类型（1：微信 2：支付宝）")

    private Integer payType;

    @ApiModelProperty(value = "订单状态（0：未支付 1：已支付）")

    private Integer status;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
