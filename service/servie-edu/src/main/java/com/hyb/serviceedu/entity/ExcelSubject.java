package com.hyb.serviceedu.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelSubject {

    @ExcelProperty(index = 0)
    private String oneClassificationSubject;

    @ExcelProperty(index = 1)
    private String twoClassificationSubject;
}
