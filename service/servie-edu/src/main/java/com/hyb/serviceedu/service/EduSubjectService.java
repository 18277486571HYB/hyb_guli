package com.hyb.serviceedu.service;

import com.hyb.serviceedu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyb.serviceedu.subject.SubjectClassification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-02-21
 */
public interface EduSubjectService extends IService<EduSubject> {

    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) throws Exception;

    List<SubjectClassification> listAllClassification();

    List<SubjectClassification> listClassificationByKey(String id);
}
