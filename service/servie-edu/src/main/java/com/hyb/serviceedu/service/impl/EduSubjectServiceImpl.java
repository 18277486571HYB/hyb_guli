package com.hyb.serviceedu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.serviceedu.entity.EduSubject;
import com.hyb.serviceedu.entity.ExcelSubject;
import com.hyb.serviceedu.listener.ExcelSubjectListener;
import com.hyb.serviceedu.mapper.EduSubjectMapper;
import com.hyb.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyb.serviceedu.subject.SubjectClassification;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-02-21
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) throws Exception{
//        读取上传过来的Excel文件
        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream, ExcelSubject.class, new ExcelSubjectListener(eduSubjectService)).sheet().doRead();
        inputStream.close();
    }

    @Override
    public List<SubjectClassification> listAllClassification() {
//        查询一级分类
        QueryWrapper<EduSubject> queryWrapperOne = new QueryWrapper<>();
        queryWrapperOne.eq("parent_id","0");
        List<EduSubject> one = this.list(queryWrapperOne);
        if (one==null){
            return null;
        }

//        二级分类
        QueryWrapper<EduSubject> queryWrapperTwo=new QueryWrapper<>();
        queryWrapperTwo.ne("parent_id","0");
        List<EduSubject> two = this.list(queryWrapperTwo);

//        封装成List<SubjectClassification>
        List<SubjectClassification> listOne=new ArrayList<>();

        //将一级分类包装成SubjectClassification类型的集合
        for (EduSubject e :
                one) {
            SubjectClassification subjectClassification = new SubjectClassification();
//            subjectClassification.setId(e.getId());
//            subjectClassification.setTitle(e.getTitle());
            BeanUtils.copyProperties(e,subjectClassification);

            List<SubjectClassification> listTwo=new ArrayList<>();
            //将所属一级分类的二级分类包装成listTwo
            for (EduSubject d :
                    two) {
                //如果二级分类的parent_id是一级分类的id
                if (d.getParentId().equals(e.getId())){
                    //该二级分类就是该一级分类的二级分类,将其做成集合,可能有多个
                    SubjectClassification subjectClassificationTwo = new SubjectClassification();
                    BeanUtils.copyProperties(d,subjectClassificationTwo);
                    listTwo.add(subjectClassificationTwo);
                }
            }
            //将每个一级分类下的所有二级分类(集合)放到Children中去
            subjectClassification.setChildren(listTwo);

            listOne.add(subjectClassification);
        }
        return listOne;
    }

    @Override
    public List<SubjectClassification> listClassificationByKey(String id) {
        return null;
    }
}
