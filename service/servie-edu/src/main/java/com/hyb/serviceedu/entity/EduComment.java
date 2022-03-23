package com.hyb.serviceedu.entity;

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
 * 评论
 * </p>
 *
 * @author hyb
 * @since 2022-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EduComment对象", description="评论")
public class EduComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "课程id")
    @NotBlank(message = "课程id不能为空")
    private String courseId;

    @ApiModelProperty(value = "会员id")
    @NotBlank(message = "用户id不能为空")
    private String memberId;

    @ApiModelProperty(value = "给谁回复,0默认给本课程回复")
    @NotBlank(message = "回复人的id不能为空")
    private String repMemberId;

    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    @ApiModelProperty(value = "0代表属于一级评论,非0代表非一级评论,这个时候值代表属于哪个一级评论")
    @NotBlank(message = "parentId不能为空")
    private String parentId;

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
