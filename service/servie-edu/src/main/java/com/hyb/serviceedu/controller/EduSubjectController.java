package com.hyb.serviceedu.controller;


import com.hyb.CommonUtil.Msg;
import com.hyb.serviceedu.service.EduSubjectService;
import com.hyb.serviceedu.service.EduTeacherService;
import com.hyb.serviceedu.subject.SubjectClassification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-02-21
 */
@RestController
@RequestMapping("/serviceedu/eduSubject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;


    @PostMapping("/upExcel")
    public Msg upExcel(MultipartFile file) throws IOException {


        try {
            eduSubjectService.saveSubject(file,eduSubjectService);
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.fail();
        }

        return Msg.success();
    }

    @GetMapping("/listSubject")
    public Msg listSubject(){
        List<SubjectClassification> list=eduSubjectService.listAllClassification();
        return list==null?Msg.fail().message("一级分类为空"):Msg.success().data("list",list);
    }
}

