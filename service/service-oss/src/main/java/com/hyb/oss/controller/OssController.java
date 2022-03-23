package com.hyb.oss.controller;

import com.hyb.CommonUtil.Msg;
import com.hyb.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "文件上传")
@RestController
@RequestMapping("/serviceOss/upFile")
@CrossOrigin
public class OssController {

    @Autowired
    OssService ossService;


    @ApiOperation("头像上传")
    @PostMapping("/avatar")
    public Msg avatar(MultipartFile multipartFile){

        String url= null;
        try {
            url = ossService.upAvatarFile(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url==null?Msg.fail():Msg.success().data("url",url);
    }



}
