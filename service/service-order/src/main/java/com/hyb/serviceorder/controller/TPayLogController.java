package com.hyb.serviceorder.controller;


import com.hyb.CommonUtil.ErrorUtil;
import com.hyb.CommonUtil.Msg;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceorder.entity.TOrder;
import com.hyb.serviceorder.service.TPayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-03-19
 */
@Api(tags = "支付记录controller")
@RestController
@RequestMapping("/serviceorder/paylog")
@CrossOrigin
public class TPayLogController {

    @Autowired
    TPayLogService payLogService;

    @ApiOperation("支付一条订单")
    @PostMapping("/insertAndUpdatePay")
    public Msg insertAndUpdatePay(@Valid @RequestBody TOrder order, BindingResult result){
        Msg error = ErrorUtil.getError(result, "map", "数据格式错误");
        if (!error.getSuccess()){
            return Msg.fail().message(error.getMessage());
        }
        //TODO: 微信扫描支付,存入map集合里,用map判断是否支付成功
        try{
            payLogService.insertAndUpdatePayLog(order);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return Msg.success();
    }

}

