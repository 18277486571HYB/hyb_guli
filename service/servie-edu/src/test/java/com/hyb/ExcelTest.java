package com.hyb;

import com.alibaba.excel.EasyExcel;
import com.hyb.easy.ExcelDemo;
import com.hyb.easy.ExcelListener;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ExcelTest {

    @Test
    public void testWriteExcel(){
        String fileName="E:\\javaStudy\\StudyingProject\\excelTest1.xlsx";
        List<ExcelDemo> excelDemos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ExcelDemo excelDemo = new ExcelDemo();
            excelDemo.setId(i);
            excelDemo.setName("hyb"+i);
            excelDemos.add(excelDemo);
        }
        //该方法自动关闭流
        EasyExcel.write(fileName, ExcelDemo.class).sheet("学生列表").doWrite(excelDemos);
    }

    @Test
    public void testReadExcel(){
        String fileName="E:\\javaStudy\\StudyingProject\\excelTest1.xlsx";

        EasyExcel.read(fileName,ExcelDemo.class, new ExcelListener()).sheet().doRead();
    }
}
