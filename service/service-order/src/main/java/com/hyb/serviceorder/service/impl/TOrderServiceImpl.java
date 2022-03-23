package com.hyb.serviceorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.CommonUtil.EduCoursePublishQuery;
import com.hyb.CommonUtil.UcenterMember;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceorder.client.OrderClient;
import com.hyb.serviceorder.client.UserClient;
import com.hyb.serviceorder.entity.TOrder;
import com.hyb.serviceorder.mapper.TOrderMapper;
import com.hyb.serviceorder.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-03-19
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    OrderClient orderClient;

    @Autowired
    UserClient userClient;

    @Override
    public String createOrder(String courseId, HttpServletRequest request) {
        EduCoursePublishQuery course=null;
        UcenterMember ucenterMember=null;
        try{
            course=orderClient.getCourseForCourse(courseId);
            ucenterMember=userClient.getUserInfo(request.getHeader("token"));
        }catch (HybException hybException){
            throw new HybException(20001,hybException.getMsg());
        }
        if (course==null){
            throw new HybException(20001,"课程信息为空");
        }
        if (ucenterMember==null){
            throw new HybException(20001,"用户信息为空");
        }
        int i = this.countOrder(ucenterMember.getId(), course.getId());
        if (i>0){
            throw new HybException(20001,"你已经生成过此订单");
        }
        TOrder order = new TOrder();
        String orderId = ucenterMember.getId() + System.currentTimeMillis();
        order.setOrderNo(orderId);
        order.setCourseId(course.getId());
        order.setCourseTitle(course.getTitle());
        order.setCourseCover(course.getCover());
        order.setMemberId(ucenterMember.getId());
        order.setMobile(ucenterMember.getMobile());
        order.setNickname(ucenterMember.getNickname());
        order.setTeacherName(course.getTeacherName());
        order.setPayType(1);
        order.setStatus(0);
        boolean save = this.save(order);
        if (!save){
            throw new HybException(20001,"未知异常");
        }
        return orderId;
    }

    @Override
    public boolean isPerchase(String courseId, String userId) {
        int i = this.countOrder(userId, courseId);
        return i>0;
    }

    //判断该课程被同一个人是否已经支付过
    private int countOrder(String userId,String courseId){
        QueryWrapper<TOrder> tOrderQueryWrapper = new QueryWrapper<>();
        tOrderQueryWrapper.eq("course_id",courseId);
        tOrderQueryWrapper.eq("member_id",userId);
        return this.count(tOrderQueryWrapper);
    }
}
