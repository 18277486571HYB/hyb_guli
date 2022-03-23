package com.hyb.serviceorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceorder.entity.TOrder;
import com.hyb.serviceorder.entity.TPayLog;
import com.hyb.serviceorder.mapper.TPayLogMapper;
import com.hyb.serviceorder.service.TOrderService;
import com.hyb.serviceorder.service.TPayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-03-19
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    TOrderService tOrderService;

    @Override
    public void insertAndUpdatePayLog(TOrder order) {
        if (this.countPay(order.getOrderNo())>0){
            throw new HybException(20001,"订单已支付或完成");
        }
        //插入一条支付记录
        TPayLog tPayLog = new TPayLog();
        tPayLog.setOrderNo(order.getOrderNo());
        tPayLog.setTotalFee(order.getTotalFee());
        tPayLog.setTransactionId(order.getMemberId()+System.currentTimeMillis());
        tPayLog.setPayType(order.getPayType());
        tPayLog.setTradeState("1");
        tPayLog.setPayTime(new Date());
        tPayLog.setAttr(order.getNickname());
        boolean save = this.save(tPayLog);
        if (!save){
            throw new HybException(20001,"支付记录异常");
        }
        //更新订单状态
        order.setStatus(1);
        boolean b = tOrderService.updateById(order);
        if (!b){
            throw new HybException(20001,"订单更新异常");
        }


    }

    //判断支付记录里是否存在该订单
    private int countPay(String orderId){
        QueryWrapper<TPayLog> tPayLogQueryWrapper = new QueryWrapper<>();
        tPayLogQueryWrapper.eq("order_no",orderId);
        return this.count(tPayLogQueryWrapper);
    }

}
