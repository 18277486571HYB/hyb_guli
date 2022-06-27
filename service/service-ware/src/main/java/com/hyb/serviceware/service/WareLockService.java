package com.hyb.serviceware.service;

import com.hyb.serviceware.entity.TOrder;
import com.hyb.serviceware.entity.WareLock;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyb
 * @since 2022-05-25
 */
public interface WareLockService extends IService<WareLock> {

    void lock(TOrder tOrder);
}
