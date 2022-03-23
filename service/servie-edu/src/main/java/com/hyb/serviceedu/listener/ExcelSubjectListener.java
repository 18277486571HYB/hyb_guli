package com.hyb.serviceedu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.entity.EduSubject;
import com.hyb.serviceedu.entity.ExcelSubject;
import com.hyb.serviceedu.service.EduSubjectService;

public class ExcelSubjectListener extends AnalysisEventListener<ExcelSubject> {

    private EduSubjectService eduSubjectService;

    public ExcelSubjectListener() {
    }

    public ExcelSubjectListener(EduSubjectService eduSubjectService){
        this.eduSubjectService=eduSubjectService;
    }




//    判断一级分类是否有重复
    public EduSubject existOneSubject(String name){
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("title",name);
        eduSubjectQueryWrapper.eq("parent_id","0");
        return eduSubjectService.getOne(eduSubjectQueryWrapper);
    }
//    判断二级分类是否有重复
    public EduSubject existTwoSubject(String name,String parentId){
        QueryWrapper<EduSubject> eduSubjectQueryWrapper = new QueryWrapper<>();
        eduSubjectQueryWrapper.eq("title",name);
        eduSubjectQueryWrapper.eq("parent_id",parentId);
        return eduSubjectService.getOne(eduSubjectQueryWrapper);
    }


    @Override
    public void invoke(ExcelSubject excelSubject, AnalysisContext analysisContext) {
        if (excelSubject==null){
            throw new HybException(20001,"文件数据为空");
        }
//        添加一级分类
        //一级分类名称
        String oneClassificationSubject = excelSubject.getOneClassificationSubject();
        EduSubject eduSubject = this.existOneSubject(oneClassificationSubject);
        if (eduSubject==null){
            eduSubject=new EduSubject();
            eduSubject.setParentId("0");
            eduSubject.setTitle(oneClassificationSubject);
            eduSubjectService.save(eduSubject);
        }

//        添加二级分类
        //二级分类的parent_id是上一级分类的id
        String parentId=eduSubject.getId();
        String twoClassificationSubject = excelSubject.getTwoClassificationSubject();
        EduSubject twoEduSubject = this.existTwoSubject(twoClassificationSubject, parentId);
        if (twoEduSubject==null){
            twoEduSubject=new EduSubject();
            twoEduSubject.setTitle(twoClassificationSubject);
            twoEduSubject.setParentId(parentId);
            eduSubjectService.save(twoEduSubject);
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
