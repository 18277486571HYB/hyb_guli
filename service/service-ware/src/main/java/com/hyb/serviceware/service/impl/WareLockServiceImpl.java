package com.hyb.serviceware.service.impl;

import com.hyb.serviceware.entity.TOrder;
import com.hyb.serviceware.entity.WareLock;
import com.hyb.serviceware.mapper.WareLockMapper;
import com.hyb.serviceware.service.WareLockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyb
 * @since 2022-05-25
 */
@Service
public class WareLockServiceImpl extends ServiceImpl<WareLockMapper, WareLock> implements WareLockService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void lock(TOrder tOrder) {
        WareLock wareLock = new WareLock();
        wareLock.setCourseId(tOrder.getCourseId());
        wareLock.setLockNum(1);
        wareLock.setOrderId(tOrder.getOrderNo());
        boolean save = this.save(wareLock);
        //锁定库存成功
        if (save){
            //发送消息锁定库存成功
            rabbitTemplate.convertAndSend("ware-event-exchange","ware.create.ware",wareLock);
        }
    }
}
