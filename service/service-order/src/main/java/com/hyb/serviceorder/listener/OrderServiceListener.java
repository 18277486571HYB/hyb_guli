package com.hyb.serviceorder.listener;

import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceorder.entity.TOrder;
import com.hyb.serviceorder.service.TOrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "order.release.order.queue")
public class OrderServiceListener {

    @Autowired
    TOrderService tOrderService;

    @RabbitHandler
    public void listener(TOrder order, Channel channel, Message message) throws IOException {

        //接收到消息,取消订单
        try {
            tOrderService.closedOrder(order);
            //手动确认,哪个消息,是否批量保存
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (HybException hybException){
            //取消订单失败,直接拒绝该消息,重回消息队列进行重试
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

   }
}
