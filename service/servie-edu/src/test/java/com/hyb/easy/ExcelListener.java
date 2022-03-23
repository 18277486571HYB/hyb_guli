package com.hyb.easy;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<ExcelDemo> {

    //具体读取方法,一行一行读取
    @Override
    public void invoke(ExcelDemo excelDemo, AnalysisContext analysisContext) {
        System.out.println("数据是--->"+excelDemo);
    }

//    读取表头
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头是--->"+headMap);
    }

//    读取之后的操作
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取成功");
    }
}
