package com.hyb.serviceorder.service;

import com.hyb.serviceorder.entity.TOrder;
import com.hyb.serviceorder.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author hyb
 * @since 2022-03-19
 */
public interface TPayLogService extends IService<TPayLog> {

    void insertAndUpdatePayLog(TOrder order);
}
