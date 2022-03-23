package com.hyb.CommonUtil;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "课程发布信息")
@Data
public class EduCoursePublishQuery {

    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String description;
    private Long buyCount;
    private Long viewCount;
    private String subjectLevelOneId;
    private String subjectLevelOne;
    private String subjectLevelTwoId;
    private String subjectLevelTwo;
    private String teacherId;
    private String teacherName;
    private String teacherAvatar;
    private String intro;
    private String career;
    private BigDecimal price;//只用于显示
    private Date gmtCreate;
    private Date gmtModified;
}
