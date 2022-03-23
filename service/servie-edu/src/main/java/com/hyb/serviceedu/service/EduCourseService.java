package com.hyb.serviceedu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hyb.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyb.serviceedu.entity.vo.EduCoursePublishQuery;
import com.hyb.serviceedu.entity.vo.EduCourseQuery;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
public interface EduCourseService extends IService<EduCourse> {

    EduCourse saveCourse(EduCourseQuery eduCourseQuery);

    EduCourseQuery getCourseById(String courseId);

    int updateCourse(EduCourseQuery eduCourseQuery);

    EduCoursePublishQuery getPublishCourseInfo(String id);

    void delCourse(String id);

    IPage<EduCourse> getCourseForKey(Integer current, Integer limit, EduCourse eduCourse);


    List<EduCoursePublishQuery> getCourseByTeacherId(String id);
}
