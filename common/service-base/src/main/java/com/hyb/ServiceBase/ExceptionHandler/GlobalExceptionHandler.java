package com.hyb.ServiceBase.ExceptionHandler;

import com.hyb.CommonUtil.Msg;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class) //指定异常处理,Exception为全部异常
    @ResponseBody
    public Msg globalExceptionHandler(Exception e){
        e.printStackTrace();
        return Msg.fail().data("error message","未知异常");
    }

    @ExceptionHandler(HybException.class)
    @ResponseBody
    public Msg HybExceptionHandler(HybException hybException){
        return Msg.fail().data("code",hybException.getCode()).data("error message",hybException.getMsg());
    }
}
