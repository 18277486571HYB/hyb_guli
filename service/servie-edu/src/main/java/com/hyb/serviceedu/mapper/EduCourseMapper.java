package com.hyb.serviceedu.mapper;

import com.hyb.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyb.serviceedu.entity.vo.EduCoursePublishQuery;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    public EduCoursePublishQuery getPublishCourseInfo(String courseId);

    public List<EduCoursePublishQuery> getPublishCourseInfoByTeacherId(String teacherId);
}
