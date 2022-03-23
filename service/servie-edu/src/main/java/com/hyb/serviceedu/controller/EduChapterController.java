package com.hyb.serviceedu.controller;


import com.hyb.CommonUtil.Msg;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.entity.EduChapter;
import com.hyb.serviceedu.service.EduChapterService;
import com.hyb.serviceedu.subject.SubjectClassification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
@Api(tags="课程大纲")
@RestController
@RequestMapping("/serviceedu/eduChapter")
@CrossOrigin

public class EduChapterController {

    @Autowired
    EduChapterService eduChapterService;

    @ApiOperation("获取全部章节和小节")
    @GetMapping("/chapter/{courseId}")
    public Msg getChapter(@ApiParam(value = "章节id", name = "courseId") @PathVariable String courseId) {

        List<SubjectClassification> list = eduChapterService.getChapters(courseId);
        return list != null ? Msg.success().data("list", list) : Msg.fail().message("章节为空");
    }

    @ApiOperation("添加章节")
    @PostMapping("/addChapter")
    public Msg addChapter(@ApiParam(value = "新章节", name = "eduChapter") @RequestBody EduChapter eduChapter) {
        EduChapter chapter=null;
        try{
            chapter= eduChapterService.addChapter(eduChapter);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return chapter == null ? Msg.fail().message("添加章节失败") : Msg.success().data("chapter", chapter);
    }

    @ApiOperation("根据id查询章节")
    @GetMapping("/getChapterById/{chapterId}")
    public Msg getChapterById(@ApiParam(value = "章节id", name = "chapterId") @PathVariable String chapterId) {
        EduChapter eduChapter=eduChapterService.getChapterById(chapterId);
        return eduChapter==null?Msg.fail().message("没此章节"):Msg.success().data("chapter",eduChapter);
    }

    @ApiOperation("更新章节")
    @PostMapping("/updateChapter")
    public Msg updateChapter(@ApiParam(value = "修改后的章节",name = "eduChapter")@RequestBody EduChapter eduChapter){
        boolean flag=eduChapterService.updateChapter(eduChapter);
        return flag?Msg.success().message("更新成功"):Msg.fail().message("更新失败");
    }

    @ApiOperation("删除章节")
    @DeleteMapping("/delChapterById/{chapterId}")
    public Msg delChapterById(@ApiParam(value = "章节id",name = "chapterId")@PathVariable String chapterId){
        boolean flag;
        try{
            flag=eduChapterService.delChapter(chapterId);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return flag?Msg.success().message("删除成功"):Msg.fail().message("删除失败");
    }
}

