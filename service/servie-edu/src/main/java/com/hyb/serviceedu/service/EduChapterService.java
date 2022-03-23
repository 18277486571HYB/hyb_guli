package com.hyb.serviceedu.service;

import com.hyb.serviceedu.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyb.serviceedu.subject.SubjectClassification;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
public interface EduChapterService extends IService<EduChapter> {

    List<SubjectClassification> getChapters(String courseId);

    EduChapter addChapter(EduChapter eduChapter);

    EduChapter getChapterById(String chapterId);

    boolean updateChapter(EduChapter eduChapter);

    boolean delChapter(String chapterId);

    void delChapterByCourseId(String id);
}
