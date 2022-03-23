package com.hyb.serviceedu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.hyb.serviceedu.entity.EduSubject;
import com.hyb.serviceedu.entity.EduTeacher;
import com.hyb.serviceedu.entity.ExcelSubject;
import com.hyb.serviceedu.listener.ExcelSubjectListener;
import com.hyb.serviceedu.mapper.EduTeacherMapper;
import com.hyb.serviceedu.service.EduSubjectService;
import com.hyb.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-02-12
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

}
