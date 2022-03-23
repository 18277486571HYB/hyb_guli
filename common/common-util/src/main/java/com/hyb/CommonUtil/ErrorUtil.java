package com.hyb.CommonUtil;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorUtil {

    public static Msg getError(BindingResult result, String field,String msg) {

        Map<String,Object> map=null;
        if (result.hasErrors()) {
            map=new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors
            ) {
                map.put(error.getField(), error.getDefaultMessage());
            }
            return Msg.fail().data(field, map).message(msg);
        }
        return Msg.success();
    }
}
