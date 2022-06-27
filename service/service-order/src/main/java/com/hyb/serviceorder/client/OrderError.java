package com.hyb.serviceorder.client;

import com.hyb.CommonUtil.EduCoursePublishQuery;
import org.springframework.stereotype.Component;


@Component
public class OrderError implements OrderClient{
    @Override
    public EduCoursePublishQuery getCourseForCourse(String courseId) {
        return null;
    }
}
