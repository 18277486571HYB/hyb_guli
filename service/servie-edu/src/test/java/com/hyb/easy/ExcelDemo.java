package com.hyb.easy;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class ExcelDemo {

    //生成列名称
    @ExcelProperty(value = "学生id",index = 0)
    private Integer id;

    @ExcelProperty(value = "学生名称",index = 1)
    private String name;
}
