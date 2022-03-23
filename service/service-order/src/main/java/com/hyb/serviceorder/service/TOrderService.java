package com.hyb.serviceorder.service;

import com.hyb.serviceorder.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-03-19
 */
public interface TOrderService extends IService<TOrder> {

    String createOrder(String courseId, HttpServletRequest request);

    boolean isPerchase(String courseId, String userId);
}
