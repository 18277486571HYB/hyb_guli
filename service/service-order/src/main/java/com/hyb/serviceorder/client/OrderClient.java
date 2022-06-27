package com.hyb.serviceorder.client;

import com.hyb.CommonUtil.EduCoursePublishQuery;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-edu",fallback = OrderError.class)
public interface OrderClient {

    @GetMapping("/serviceedu/eduCourse/getCourseByIdForCourse/{courseId}")
    public EduCoursePublishQuery getCourseForCourse(@PathVariable("courseId") String courseId);
}
