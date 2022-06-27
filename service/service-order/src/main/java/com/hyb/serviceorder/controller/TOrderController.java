package com.hyb.serviceorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hyb.CommonUtil.JwtUtils;
import com.hyb.CommonUtil.Msg;
import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceorder.entity.TOrder;
import com.hyb.serviceorder.service.TOrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author hyb
 * @since 2022-03-19
 */
@Api(tags = "订单controller")
@RestController
@RequestMapping("/serviceorder/torder")
@CrossOrigin
public class TOrderController {



    @Autowired
    TOrderService orderService;

    @ApiOperation("生成订单")
    @PostMapping("/insertOrder/{courseId}")
    public Msg insertOrder(@PathVariable String courseId, HttpServletRequest request){
        String token = request.getHeader("token");
        String s=null;
        try{
            s=orderService.createOrder(courseId, request);
        }catch (HybException hybException){
            return Msg.fail().message(hybException.getMsg());
        }
        return s==null?Msg.fail():Msg.success().data("orderId",s);

    }
    @ApiOperation("获取订单信息")
    @GetMapping("/getOrderInfo/{orderId}")
    public Msg getOrderInfo(@PathVariable String orderId){
        QueryWrapper<TOrder> tOrderQueryWrapper = new QueryWrapper<>();
        tOrderQueryWrapper.eq("order_no",orderId);
        TOrder one = orderService.getOne(tOrderQueryWrapper);
        return one==null?Msg.fail():Msg.success().data("order",one);

    }

    @ApiOperation("订单是否已经被购买")
    @GetMapping("/isPerchase/{courseId}/{userId}")
    public Msg isPerchase(@PathVariable String courseId,@PathVariable String userId){

        boolean isPerchase=orderService.isPerchase(courseId,userId);
        return isPerchase?Msg.success():Msg.fail();

    }

    @GetMapping("/getStatus/{orderNo}")
    public Integer getStatus(@PathVariable("orderNo") String orderNo){
        TOrder byId = orderService.getById(orderNo);
        if (byId!=null){
            return byId.getStatus();
        }
        return -1;
    }


}

