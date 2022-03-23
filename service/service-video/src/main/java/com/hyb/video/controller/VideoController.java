package com.hyb.video.controller;

import com.hyb.CommonUtil.Msg;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.video.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "阿里云视频点播")
@RestController
@RequestMapping("/serviceVideo/serviceVideo")
@CrossOrigin
public class VideoController {

    @Autowired
    VideoService videoService;

    @ApiOperation("视频上传")
    @PostMapping("/upFile")
    public Msg upFile(@ApiParam(value = "文件",name = "file")MultipartFile file){
        String videoId = null;
        try {
            videoId=videoService.upVideoFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Msg.success().data("videoId",videoId);
    }

    @ApiOperation("删除视频")
    @DeleteMapping("/delVideo/{id}")
    public Msg delVideo(@ApiParam(value = "视频id",name = "id")@PathVariable String id){
        try {
            videoService.delVideoFile(id);
        } catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.fail().message("删除视频失败");
        }
        return Msg.success().message("删除成功");
    }

    @ApiOperation("删除多个视频")
    @DeleteMapping("/delBatchVideo")
    public Msg delBatchVideo(@RequestParam("list")List<String> list){
        try {
            videoService.delVideoFileByBatch(list);
        } catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.fail().message("删除视频失败");
        }
        return Msg.success().message("删除成功");
    }

    @ApiOperation("获取视频播放凭证")
    @GetMapping("/getPlayAuth/{id}")
    public Msg getPlayAuth(@PathVariable String id, HttpServletRequest request){

        String auth=null;
        try{
            auth=videoService.getPlayAuth(id,request);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }

        return auth==null?Msg.fail():Msg.success().data("auth",auth);
        
    }

    @ApiOperation("获取视频播放地址")
    @GetMapping("/getPlayAddress/{id}")
    public Msg getPlayAddress(@PathVariable String id,HttpServletRequest request){

        List<String> address=null;
        try{
            address=videoService.getPlayAddress(id,request);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }

        return address==null?Msg.fail():Msg.success().data("addressList",address);

    }

}
