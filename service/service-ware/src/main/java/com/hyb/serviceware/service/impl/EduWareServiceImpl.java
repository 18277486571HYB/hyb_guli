package com.hyb.serviceware.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hyb.serviceware.entity.EduWare;
import com.hyb.serviceware.entity.TOrder;
import com.hyb.serviceware.mapper.EduWareMapper;
import com.hyb.serviceware.service.EduWareService;
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
public class EduWareServiceImpl extends ServiceImpl<EduWareMapper, EduWare> implements EduWareService {

    @Override
    public void reduceWare(TOrder tOrder) {
        //减少库存
        UpdateWrapper<EduWare> eduWare = new UpdateWrapper<>();
        eduWare.setSql("course_ware=course_ware-"+1);
        EduWare wareEntity = new EduWare();
        wareEntity.setCourseId(tOrder.getCourseId());
        this.update(wareEntity, eduWare);
    }

    @Override
    public void unLockWare(String courseId) {
        //增加库存
        UpdateWrapper<EduWare> eduWare = new UpdateWrapper<>();
        eduWare.setSql("course_ware=course_ware+"+1);
        EduWare wareEntity = new EduWare();
        wareEntity.setCourseId(courseId);
        this.update(wareEntity, eduWare);
    }
}
