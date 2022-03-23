package com.hyb.serviceedu.service;

import com.hyb.serviceedu.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
public interface EduVideoService extends IService<EduVideo> {

    void addVideo(EduVideo eduVideo);

    void delVideo(String id);

    void updateVideo(EduVideo eduVideo);

    EduVideo getVideById(String id);

    void delVideoByCourseId(String id);
}
