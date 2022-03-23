package com.hyb.serviceedu.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyb.CommonUtil.Msg;
import com.hyb.serviceedu.entity.EduCourse;
import com.hyb.serviceedu.entity.EduTeacher;
import com.hyb.serviceedu.entity.vo.EduCoursePublishQuery;
import com.hyb.serviceedu.service.EduCourseService;
import com.hyb.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "前台讲师controller")
@RestController
@RequestMapping("/serviceedu/front")
@CrossOrigin
public class FrontTeacherController {

    @Autowired
    EduTeacherService eduTeacherService;

    @Autowired
    EduCourseService eduCourseService;

    //分页功能的实现
    @ApiOperation("讲师分页功能")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public Msg pageTeacher(@PathVariable Integer current, @PathVariable Integer limit){
        Page<EduTeacher> page=new Page<>(current,limit);
        QueryWrapper<EduTeacher> eduTeacherQueryWrapper = new QueryWrapper<>();
        eduTeacherQueryWrapper.orderByAsc("sort");
        IPage<EduTeacher> iPage = eduTeacherService.page(page, eduTeacherQueryWrapper);
        return iPage==null?Msg.fail():Msg.success().data("total",iPage.getTotal()).data("list",iPage.getRecords());
    }

    @ApiOperation("根据id查询讲师")
    @GetMapping("/selectTeacherById/{id}")
    public Msg selectTeacherById(@ApiParam(name = "id",value = "讲师id",required = true)@PathVariable String  id){
        EduTeacher teacher = eduTeacherService.getById(id);
        return teacher!=null?Msg.success().data("teacher",teacher):Msg.fail();
    }

    @ApiOperation("根据讲师id查询课程")
    @GetMapping("/getCourse/{id}")
    public Msg getCourse(@PathVariable String id){
        List<EduCoursePublishQuery> eduCourse=eduCourseService.getCourseByTeacherId(id);
        return eduCourse==null?Msg.fail():Msg.success().data("list",eduCourse);
    }

    @ApiOperation("查询所有讲师")
    @GetMapping("/selectAllTeacher")
    public Msg selectAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return Msg.success().data("items",list);
    }
}
