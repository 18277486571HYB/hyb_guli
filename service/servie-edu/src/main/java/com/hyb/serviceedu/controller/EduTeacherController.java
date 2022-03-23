package com.hyb.serviceedu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyb.CommonUtil.Msg;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.entity.EduTeacher;
import com.hyb.serviceedu.entity.vo.TeacherQuery;
import com.hyb.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-02-12
 */


@Api(tags = "讲师管理")
@RestController
@RequestMapping("/serviceedu/teachers")
@Slf4j
@CrossOrigin
public class EduTeacherController {

    @Autowired
    EduTeacherService eduTeacherService;
    @ApiOperation("查询所有讲师")
    @GetMapping("/selectAllTeacher")
    public Msg selectAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return Msg.success().data("items",list);
    }


    @ApiOperation("删除某个讲师")
    @DeleteMapping("/delTeacher/{id}")
    public Msg delTeacher(@ApiParam(name = "id",value = "讲师id",required = true) @PathVariable String id){
        boolean b = eduTeacherService.removeById(id);
        return b?Msg.success():Msg.fail();
    }

    //分页功能的实现
    @ApiOperation("讲师分页功能")
    @GetMapping("/pageTeacher/{current}/{limit}")
    public Msg pageTeacher(@PathVariable Integer current, @PathVariable Integer limit){
        Page<EduTeacher> page=new Page<>(current,limit);
        IPage<EduTeacher> iPage = eduTeacherService.page(page, null);
        return Msg.success().data("total",iPage.getTotal()).data("data",iPage.getRecords());
    }

    //条件分页查询
    @ApiOperation("条件分页查询讲师")
    @PostMapping("/pageTeacherForKey/{current}/{limit}")
    public Msg pageTeacherForKey(@PathVariable Integer current, @PathVariable Integer limit,
                                @RequestBody(required = false) TeacherQuery teacherQuery){
        Page<EduTeacher> page=new Page<>(current,limit);
        QueryWrapper<EduTeacher> teacherQueryWrapper = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)){
            teacherQueryWrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            teacherQueryWrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            teacherQueryWrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            teacherQueryWrapper.le("gmt_modified",end);
        }
        //排序,每次插入的都排在最前面让
        teacherQueryWrapper.orderByDesc("gmt_create");

        IPage<EduTeacher> iPage = eduTeacherService.page(page, teacherQueryWrapper);
        long total = iPage.getTotal();
        List<EduTeacher> records = iPage.getRecords();
        return Msg.success().data("total",total).data("items",records);
    }

    //添加讲师的功能
    @ApiOperation("添加讲师")
    @PostMapping("/addTeacher")
    public Msg addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        return save?Msg.success():Msg.fail();
    }

    @ApiOperation("根据id查询讲师")
    @GetMapping("/selectTeacherById/{id}")
    public Msg selectTeacherById(@ApiParam(name = "id",value = "讲师id",required = true)@PathVariable String  id){
        log.info("根据id查询讲师----->");
        EduTeacher teacher = eduTeacherService.getById(id);
        log.info("返回结果:{}",teacher);
        return teacher!=null?Msg.success().data("teacher",teacher):Msg.fail();
    }

    @ApiOperation("根据id修改讲师")
    @PutMapping("/updateTeacherById/{id}")
    public Msg updateTeacherById(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable(value = "id")String id,
            @RequestBody EduTeacher eduTeacher
            ){
        eduTeacher.setId(id);
        boolean b = eduTeacherService.updateById(eduTeacher);
        return b?Msg.success():Msg.fail();
    }

    @ApiOperation("根据id修改讲师")
    @PostMapping("/updateTeacherById")
    public Msg updateTeacher(
            @ApiParam(name = "eduTeacher",value = "讲师信息",required = true)
            @RequestBody EduTeacher eduTeacher
            ){
        boolean b = eduTeacherService.updateById(eduTeacher);
        return b?Msg.success():Msg.fail();
    }

}

