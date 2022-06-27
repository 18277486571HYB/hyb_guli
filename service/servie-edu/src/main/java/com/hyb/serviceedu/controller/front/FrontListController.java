package com.hyb.serviceedu.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.CommonUtil.Msg;
import com.hyb.serviceedu.entity.EduCourse;
import com.hyb.serviceedu.entity.EduTeacher;
import com.hyb.serviceedu.service.EduCourseService;
import com.hyb.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.apache.velocity.runtime.parser.node.ASTIntegerLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "前台课程和讲师数据展示")
@RestController
@RequestMapping("/serviceedu/front")
@CrossOrigin
public class FrontListController {

    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    EduTeacherService eduTeacherService;

    @ApiOperation("课程展示")
    @GetMapping("/listCourse/{limit}")
    public Msg listCourse(@ApiParam(value = "列出课程数量",name = "limit")@PathVariable(required = false)Integer limit){
        if (StringUtils.isEmpty(limit)){
            limit=8;
        }


        List<EduCourse> list = eduCourseService.listCourse(limit);

        return list.size()==0?Msg.fail():Msg.success().data("list",list);

    }

    @ApiOperation("讲师展示")
    @GetMapping("/listTeacher/{limit}")
    public Msg listTeacher(@ApiParam(value = "列出讲师数量",name = "limit")@PathVariable(required = false)Integer limit){
        if (StringUtils.isEmpty(limit)){
            limit=4;
        }
        QueryWrapper<EduTeacher> eduCourseQueryWrapper = new QueryWrapper<>();

        eduCourseQueryWrapper.orderByDesc("level");

        eduCourseQueryWrapper.last("limit "+limit);

        List<EduTeacher> list = eduTeacherService.list(eduCourseQueryWrapper);

        return list.size()==0?Msg.fail():Msg.success().data("list",list);

    }
}
