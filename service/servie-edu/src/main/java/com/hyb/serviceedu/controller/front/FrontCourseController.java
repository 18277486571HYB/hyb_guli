package com.hyb.serviceedu.controller.front;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hyb.CommonUtil.Msg;
import com.hyb.serviceedu.entity.EduCourse;
import com.hyb.serviceedu.entity.EduTeacher;
import com.hyb.serviceedu.entity.vo.EduCoursePublishQuery;
import com.hyb.serviceedu.service.EduChapterService;
import com.hyb.serviceedu.service.EduCourseService;
import com.hyb.serviceedu.service.EduTeacherService;
import com.hyb.serviceedu.subject.SubjectClassification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("前台课程controller")
@RestController
@RequestMapping("/serviceedu/front")
@CrossOrigin
public class FrontCourseController {

    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    EduChapterService eduChapterService;

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

    @ApiOperation("根据课程id查询课程所有相关信息")
    @GetMapping("/getAllCourseInfo/{courseId}")
    public Msg getAllCourseInfo(@PathVariable String courseId){

        List<SubjectClassification> chapters = eduChapterService.getChapters(courseId);
        EduCoursePublishQuery publishCourseInfo = eduCourseService.getPublishCourseInfo(courseId);
        return Msg.success().data("course",publishCourseInfo).data("list",chapters);
    }

}
