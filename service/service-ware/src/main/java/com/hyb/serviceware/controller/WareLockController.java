package com.hyb.serviceware.controller;


import com.hyb.CommonUtil.Msg;
import com.hyb.serviceware.entity.TOrder;
import com.hyb.serviceware.service.WareLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-05-25
 */
@RestController
@RequestMapping("/serviceware/warelock")
public class WareLockController {


    @Autowired
    WareLockService lockService;

    @PostMapping("/lock")
    public Msg lock(@RequestBody TOrder tOrder){
        lockService.lock(tOrder);
        return Msg.success();
    }
}

