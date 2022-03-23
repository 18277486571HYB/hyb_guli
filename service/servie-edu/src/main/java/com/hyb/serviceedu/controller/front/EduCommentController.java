package com.hyb.serviceedu.controller.front;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hyb.CommonUtil.ErrorUtil;
import com.hyb.CommonUtil.Msg;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.entity.EduComment;
import com.hyb.serviceedu.entity.vo.EduCommentQuery;
import com.hyb.serviceedu.service.EduCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-03-07
 */
@RestController
@RequestMapping("/serviceedu/eduComment")
@CrossOrigin
public class EduCommentController {

    @Autowired
    EduCommentService eduCommentService;

    @ApiOperation("增加一条评论")
    @PostMapping("/insertComment")
    public Msg insert(@Valid @RequestBody EduComment eduComment, BindingResult result, HttpServletRequest request){

        Msg error = ErrorUtil.getError(result, "map", "数据格式错误");
        if (!error.getSuccess()){
            return error;
        }
        boolean flag;
        try{
            flag=eduCommentService.insertComment(eduComment,request);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }

        return flag?Msg.success():Msg.fail();
    }
    @ApiOperation("增加一条评论")
    @PostMapping("/testInsertComment")
    public Msg testInsert(@RequestBody EduComment eduComment){
        boolean save = eduCommentService.save(eduComment);
        return save?Msg.success():Msg.fail();
    }


    @ApiOperation("查询某课程的所有评论")
    @GetMapping("/getAllComment/{page}/{limit}/{courseId}")
    public Msg getAllComment(@PathVariable(required = false)Integer page,
                             @PathVariable(required = false)Integer limit,
                             @PathVariable(required = false) String courseId){



        int total=eduCommentService.getTotal(courseId);

        if (total==0){
            Msg.fail().message("该课程暂时没有评论");
        }

        List<EduCommentQuery> iPage=eduCommentService.getAllComment(page,limit,courseId);


        return Msg.success().data("total",total).data("list",iPage);
    }
}

