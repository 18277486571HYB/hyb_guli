package com.hyb.serviceedu.entity.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.hyb.serviceedu.entity.EduComment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "评论基本信息", description = "评论基本信息vo类")
public class EduCommentQuery {

    @ApiModelProperty(value = "评论id")
    private String id;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "用户名")
    private String nickname;

    @ApiModelProperty(value = "给谁回复,0默认给本课程回复")
    private String repMemberId;

    private String repNickName;

    @ApiModelProperty(value = "评论内容")
    private String content;

    private String parentId;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;


    @ApiModelProperty("子评论")
    private List<EduCommentQuery> children;

}
