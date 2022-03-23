package com.hyb.serviceedu.mapper;

import com.hyb.serviceedu.entity.EduComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyb.serviceedu.entity.vo.EduCommentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论 Mapper 接口
 * </p>
 *
 * @author hyb
 * @since 2022-03-07
 */
public interface EduCommentMapper extends BaseMapper<EduComment> {

    public List<EduCommentQuery> getFirstEduComment(@Param("parentId")String parentId,@Param("courseId") String courseId,@Param("low") Integer low, @Param("size") Integer size);
    public List<EduCommentQuery> getSecondEduComment(@Param("parentId") String parentId,@Param("courseId") String courseId);
}
