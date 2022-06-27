package com.hyb.serviceware.listener;

import com.hyb.ServiceBase.ExceptionHandler.HybException;
import com.hyb.serviceware.client.OrderClient;
import com.hyb.serviceware.entity.TOrder;
import com.hyb.serviceware.entity.WareLock;
import com.hyb.serviceware.service.EduWareService;
import com.hyb.serviceware.service.WareLockService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RabbitListener(queues = "ware.release.ware.queue")
public class WareListener {

    @Autowired
    EduWareService wareService;

    @Autowired
    WareLockService lockService;

    @Autowired
    OrderClient orderClient;

    @RabbitHandler
    public void listener(WareLock wareLock, Message message, Channel channel) throws IOException {
        //如果该订单存在锁定表就执行相应逻辑,不然可能是已经支付了的或者手动取消了
        try{
            WareLock byId = lockService.getById(wareLock.getOrderId());
            if (byId!=null){
                //如果该订单状态是已取消的,才能回滚库存
                Integer status = orderClient.getStatus(wareLock.getOrderId());
                //订单不存在或者订单被取消都要解锁库存
                //因为订单可能先回滚了,由于没有用到事务,所以库存还是有的
                if (status==3||status==-1){
                    wareService.unLockWare(wareLock.getCourseId());
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
                }
            }
        }catch (HybException | IOException e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }


    /*
    * 防止因为订单延迟释放导致库存释放不了
    * 所以要在订单延迟释放之后再次释放一次库存
    * */
    @RabbitHandler
    public void listener(TOrder tOrder, Message message, Channel channel) throws IOException {
        //但是要注意的是只能释放未解锁的库存,因为有些之前已经释放了
        // 传过来的tOrder一定是从已经释放的订单里过来的,所以不用判断订单状态
        try{
           //TODO 查询库存锁定表里锁定状态
            //TODO 如果该订单是未解锁的,进行解锁,且库存回滚
            //TODO 如果该订单已经是解锁状态,说明之前的释放顺序一切正常
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (HybException e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }
    }


}
