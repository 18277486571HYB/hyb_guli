package com.hyb.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.entity.EduChapter;
import com.hyb.serviceedu.entity.EduCourse;
import com.hyb.serviceedu.entity.EduVideo;
import com.hyb.serviceedu.mapper.EduChapterMapper;
import com.hyb.serviceedu.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyb.serviceedu.service.EduCourseService;
import com.hyb.serviceedu.service.EduVideoService;
import com.hyb.serviceedu.subject.SubjectClassification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-02-22
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService eduVideoService;

    @Autowired
    EduCourseService eduCourseService;

    @Override
    public List<SubjectClassification> getChapters(String courseId) {
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id",courseId);
        List<EduChapter> eduChapterList = this.list(eduChapterQueryWrapper);
        if (eduChapterList==null){
            return null;
        }
        List<SubjectClassification> list=new ArrayList<>();
        for (EduChapter e :
                eduChapterList) {
            SubjectClassification subjectClassification = new SubjectClassification();
            BeanUtils.copyProperties(e,subjectClassification);
            QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
            eduVideoQueryWrapper.eq("chapter_id",e.getId());
            List<EduVideo> eduVideoList = eduVideoService.list(eduVideoQueryWrapper);
            List<SubjectClassification> videoList=new ArrayList<>();
            for (EduVideo v :
                    eduVideoList) {
                SubjectClassification subjectClassificationVideo = new SubjectClassification();
                BeanUtils.copyProperties(v,subjectClassificationVideo);
                videoList.add(subjectClassificationVideo);
            }
            subjectClassification.setChildren(videoList);
            list.add(subjectClassification);
        }
        return list;
    }

    @Override
    public EduChapter addChapter(EduChapter eduChapter) {
//        判断课程id是否存在
        EduCourse eduCourse = this.getEduCourseById(eduChapter.getCourseId());
        if (eduCourse==null){
            throw new HybException(20001,"课程id不存在");
        }
        boolean save = this.save(eduChapter);
        System.out.println(save);
        return save?eduChapter:null;
    }

    private EduCourse getEduCourseById(String id){
        return eduCourseService.getById(id);
    }

    @Override
    public EduChapter getChapterById(String chapterId) {
        return this.getById(chapterId);
    }

    @Override
    public boolean updateChapter(EduChapter eduChapter) {
        return this.updateById(eduChapter);
    }

    @Override
    public boolean delChapter(String chapterId) {
//        根据章节id查询小节,如果有小节就不可以删除章节,需要将章节删除
        QueryWrapper<EduVideo> eduVideoQueryWrapper = new QueryWrapper<>();
        eduVideoQueryWrapper.eq("id", chapterId);
        int count = eduVideoService.count(eduVideoQueryWrapper);
        if (count!=0) {
            throw new HybException(20001, "此章节存在小节,不能删除");
        }
        return this.removeById(chapterId);
    }

    @Override
    public void delChapterByCourseId(String id) {
        QueryWrapper<EduChapter> eduChapterQueryWrapper = new QueryWrapper<>();
        eduChapterQueryWrapper.eq("course_id",id);
        boolean remove = this.remove(eduChapterQueryWrapper);
        if (!remove){
            throw new HybException(20001,"删除章节失败");
        }
    }
}
