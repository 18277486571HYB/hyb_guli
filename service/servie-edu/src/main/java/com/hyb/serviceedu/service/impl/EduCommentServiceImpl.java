package com.hyb.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hyb.CommonUtil.Msg;
import com.hyb.CommonUtil.UcenterMember;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceedu.client.CommentClient;
import com.hyb.serviceedu.client.VideoClient;
import com.hyb.serviceedu.entity.EduComment;
import com.hyb.serviceedu.entity.EduCourse;
import com.hyb.serviceedu.entity.vo.EduCommentQuery;
import com.hyb.serviceedu.entity.vo.EduCourseQuery;
import com.hyb.serviceedu.mapper.EduCommentMapper;
import com.hyb.serviceedu.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyb.serviceedu.service.EduCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-03-07
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Autowired
    EduCourseService eduCourseService;

    @Autowired
    CommentClient commentClient;

    @Override
    public boolean insertComment(EduComment eduComment, HttpServletRequest request) {

        //课程id必须存在
        EduCourseQuery eduCourse = this.getEduCourse(eduComment.getCourseId());

        if (eduCourse==null){
            throw new HybException(20001,"课程di不存在");
        }

        //如果没有默认值传过来,那么只能给当前表存在的会员id回复
        String repMemberId = eduComment.getRepMemberId();
        if(!"0".equals(repMemberId)){
            if (!isExistEduComment("member_id",repMemberId)){
                throw new HybException(20001,"用户未开启评论功能,无法回复");
            }
        }


        //如果没有默认值传过来,那么只能给当前表存在的评论id回复
        String parentId = eduComment.getParentId();
        if (!"0".equals(parentId)){
            if (!isExistEduComment("id",parentId)){
                throw new HybException(20001,"该用户不在评论组里,无法回复");
            }
        }


        //判断token是否存在

        UcenterMember token = commentClient.getUserInfo(request.getHeader("token"));

        if (token==null){
            throw new HybException(20001,"用户未登录");
        }

        eduComment.setMemberId(token.getId());

        return this.save(eduComment);
    }

    //检查表中某个字段是否存在
    private boolean isExistEduComment(String l,String v){
        QueryWrapper<EduComment> eduCommentQueryWrapper = new QueryWrapper<>();
        eduCommentQueryWrapper.eq(l,v);
        return this.count(eduCommentQueryWrapper)>0;
    }

    @Override
    public List<EduCommentQuery> getAllComment(Integer page, Integer limit, String courseId) {

        if (StringUtils.isEmpty(page)){
            page=1;
        }
        if (StringUtils.isEmpty(limit)){
            limit=5;
        }
        EduCourseQuery eduCourse = this.getEduCourse(courseId);

        if (eduCourse==null){
            throw new HybException(20001,"课程id不存在");
        }

        //TODO
        //所有一级评论和二级评论,然后根据parent_id获取属于哪个一级评论的
        List<EduCommentQuery> firstEduComment = baseMapper.getFirstEduComment("0", courseId,(page-1)*limit,page*limit);

        //根据rep_member_id知道给哪个评论的
        List<EduCommentQuery> secondEduComment = baseMapper.getSecondEduComment("0", courseId);

        //整合二级评论到一级中
        for (EduCommentQuery first :
                firstEduComment) {
            List<EduCommentQuery> list=new ArrayList<>();
            for (EduCommentQuery second :
                    secondEduComment) {
               if (second.getParentId().equals(first.getId())){
                   list.add(second);
               }
            }
            first.setChildren(list);
        }
        return firstEduComment;
    }

    @Override
    public int getTotal(String courseId) {
        QueryWrapper<EduComment> eduCommentQueryWrapper = new QueryWrapper<>();
        eduCommentQueryWrapper.eq("course_id",courseId);
        eduCommentQueryWrapper.eq("parent_id","0");
        return this.count(eduCommentQueryWrapper);
    }

    private EduCourseQuery getEduCourse(String id){
        return eduCourseService.getCourseById(id);
    }
}
