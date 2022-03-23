package com.hyb.serviceedu.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeacherQuery {

    @ApiModelProperty("教师名称,模糊查询")
    private String name;

    @ApiModelProperty("头衔,1 高级讲师 2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "查询开始时间",example = "2019-01-01 00:00:00")
    private String begin;

    @ApiModelProperty(value = "查询结束时间",example = "2019-12-30 23:59:59")
    private String end;
}
