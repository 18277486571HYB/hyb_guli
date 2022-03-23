package com.hyb.serviceedu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hyb.CommonUtil.Msg;
import com.hyb.CommonUtil.RedisUtils;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.entity.EduCourse;
import com.hyb.serviceedu.entity.vo.EduCoursePublishQuery;
import com.hyb.serviceedu.entity.vo.EduCourseQuery;
import com.hyb.serviceedu.handler.VideoHandler;
import com.hyb.serviceedu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
@Api(tags = "课程管理")
@RestController
@RequestMapping("/serviceedu/eduCourse")
@CrossOrigin
public class EduCourseController {

    @Autowired
    EduCourseService eduCourseService;

    @ApiOperation("添加课程")
    @PostMapping("/addCourse")
    public Msg addCourse(@RequestBody EduCourseQuery eduCourseQuery){
        EduCourse eduCourse = eduCourseService.saveCourse(eduCourseQuery);
        return eduCourse!=null?Msg.success().data("courseId",eduCourse.getId()):Msg.fail();
    }

    @ApiOperation("根据课程id查询课程")
    @GetMapping("/getCourseById/{courseId}")
    public Msg getCourse(@ApiParam(value = "课程id",name ="courseId" )@PathVariable String courseId){
        EduCourseQuery eduCourseQuery=eduCourseService.getCourseById(courseId);
        return eduCourseQuery==null?Msg.fail().message("课程为空"):Msg.success().data("courseInfo",eduCourseQuery);
    }

    @ApiOperation("根据课程id查询课程")
    @GetMapping("/getCourseByIdForCourse/{courseId}")
    public EduCoursePublishQuery getCourseForCourse(@PathVariable String courseId){
        return eduCourseService.getPublishCourseInfo(courseId);
    }

    @ApiOperation("更新课程信息")
    @PostMapping("/updateCourse")
    public Msg updateCourse(@ApiParam(value = "课程新信息",name = "eduCourseQuery")@RequestBody EduCourseQuery eduCourseQuery){
        int updateId=eduCourseService.updateCourse(eduCourseQuery);
        return updateId==1?Msg.success():Msg.success().message("更新失败");
    }

    @ApiOperation("获取课程最终信息")
    @GetMapping("/getPublishCourseInfo/{id}")
    public Msg getPublishCourseInfo(@ApiParam(value = "课程id",name = "id")@PathVariable String id){

        EduCoursePublishQuery eduCoursePublishQuery=eduCourseService.getPublishCourseInfo(id);

        return eduCoursePublishQuery==null?Msg.fail().message("课程最终信息为空"):Msg.success().data("list",eduCoursePublishQuery);
    }

    @ApiOperation("发布课程")
    @PostMapping("/publishCourse/{id}")
    public Msg publishCourse(@ApiParam(value = "课程id",name = "id")@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        boolean b = eduCourseService.updateById(eduCourse);

        if (b)
            RedisUtils.setHash("courseCount",id,"1",26, TimeUnit.HOURS);

        return b?Msg.success().message("发布成功"):Msg.fail().message("发布失败");
    }

    @ApiOperation("查询所有课程")
    @GetMapping("/listCourse")
    public Msg listCourse(){
        List<EduCourse> list =
                eduCourseService.list(null);
        return Msg.success().data("list",list);
    }

    @ApiOperation("删除所有课程")
//    @SentinelResource(value = "delCourse",blockHandlerClass = VideoHandler.class,blockHandler = "delHandler")
    @DeleteMapping("/delCourse/{id}")
    public Msg delCourse(@ApiParam(value = "课程id",name = "id")@PathVariable String id){
        try{
            eduCourseService.delCourse(id);
        }catch (HybException hybException){
            return Msg.fail().message("删除课程失败");
        }
        return Msg.success();
    }

    @ApiOperation("条件分页查询")
    @PostMapping("/listCourseForKey/{current}/{limit}")
    public Msg listCourseForKey(
            @ApiParam(value = "当前页",name="current")@PathVariable Integer current,
            @ApiParam(value = "每页限制数量",name = "limit")@PathVariable Integer limit,
            @ApiParam(value = "查询条件",name="eduCourseQuery")@RequestBody(required = false) EduCourse eduCourse
    ){
        List<EduCourse> list=null;
        long total=0;
        IPage<EduCourse> iPage =eduCourseService.getCourseForKey(current,limit,eduCourse);
        if (iPage!=null){
            list=iPage.getRecords();
            total = iPage.getTotal();
        }
        return list == null ?Msg.fail().message("数据为空"):Msg.success().data("list",list).data("total",total);
    }
}

