package com.hyb.serviceedu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hyb.serviceedu.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyb.serviceedu.entity.vo.EduCommentQuery;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-03-07
 */
public interface EduCommentService extends IService<EduComment> {

    boolean insertComment(EduComment eduComment, HttpServletRequest request);

    List<EduCommentQuery> getAllComment(Integer page, Integer limit, String courseId);

    int getTotal(String courseId);
}
