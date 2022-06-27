package com.hyb.serviceware.service;

import com.hyb.serviceware.entity.EduWare;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hyb.serviceware.entity.TOrder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyb
 * @since 2022-05-25
 */
public interface EduWareService extends IService<EduWare> {

    void reduceWare(TOrder tOrder);


    void unLockWare(String courseId);
}
