package com.hyb.serviceedu.controller;


import com.hyb.CommonUtil.Msg;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.entity.EduVideo;
import com.hyb.serviceedu.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
@Api(tags = "章节的小节")
@RestController
@RequestMapping("/serviceedu/eduVideo")
@CrossOrigin
public class EduVideoController {

    @Autowired
    EduVideoService eduVideoService;

    @ApiOperation("添加小节")
    @PostMapping("/addVideo")
    public Msg addVideo(@ApiParam(value = "小节",name = "eduVideo")@RequestBody EduVideo eduVideo){
        try{
            eduVideoService.addVideo(eduVideo);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return Msg.success();
    }

    @ApiOperation("删除小节")
    @DeleteMapping("/delVideo/{id}")
    public Msg delVideo(@ApiParam(value = "小节id",name = "id")@PathVariable String id){
        try{
            eduVideoService.delVideo(id);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return Msg.success();
    }

    @ApiOperation("修改小节")
    @PostMapping("/updateVideo")
    public Msg updateVideo(@ApiParam(value = "新的小节",name = "eduVideo")@RequestBody EduVideo eduVideo){
        try{
            eduVideoService.updateVideo(eduVideo);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return Msg.success();
    }

    @ApiOperation("获取小节")
    @GetMapping("/getVideoById/{id}")
    public Msg getVideoById(@ApiParam(value = "小节id",name = "id")@PathVariable String id){
        EduVideo eduVideo=null;
        try{
            eduVideo=eduVideoService.getVideById(id);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return Msg.success().data("video",eduVideo);
    }

}

