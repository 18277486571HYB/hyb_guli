package com.hyb.serviceedu.controller.front;

import com.hyb.CommonUtil.Msg;
import com.hyb.serviceedu.service.EduSubjectService;
import com.hyb.serviceedu.subject.SubjectClassification;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("前台分类查询controller")
@RestController
@RequestMapping("/serviceedu/front")
@CrossOrigin
public class FrontSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;

    @GetMapping("/listSubject")
    public Msg listSubject(){
        List<SubjectClassification> list=eduSubjectService.listAllClassification();
        return list==null?Msg.fail().message("一级分类为空"):Msg.success().data("list",list);
    }
}
