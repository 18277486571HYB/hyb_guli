package com.hyb.serviceware.config;

import com.hyb.serviceware.entity.TOrder;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;

@Configuration
public class OrderMqConfig {

    @RabbitListener(queues = "ware.release.ware.queue")
    public void listener(TOrder order, Channel channel, Message message) throws IOException {
        System.out.println("确认接收消息"+order.toString());
        //手动确认,哪个消息,是否批量保存
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }



    @Bean
    public Queue orderDelayQueue() {
        /*
            Queue(String name,  队列名字
            boolean durable,  是否持久化
            boolean exclusive,  是否排他
            boolean autoDelete, 是否自动删除
            Map<String, Object> arguments) 属性
         */
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "ware-event-exchange");
        arguments.put("x-dead-letter-routing-key", "ware.release.ware");
        arguments.put("x-message-ttl", 6000); // 消息过期时间 1分钟
        Queue queue = new Queue("ware.delay.queue", true, false, false, arguments);

        return queue;
    }

    /**
     * 普通队列
     *
     * @return
     */
    @Bean
    public Queue orderReleaseQueue() {

        Queue queue = new Queue("ware.release.ware.queue", true, false, false);

        return queue;
    }

    /**
     * TopicExchange
     *
     * @return
     */
    @Bean
    public Exchange orderEventExchange() {
        /*
         *   String name,
         *   boolean durable,
         *   boolean autoDelete,
         *   Map<String, Object> arguments
         * */
        return new TopicExchange("ware-event-exchange", true, false);

    }


    @Bean
    public Binding orderCreateBinding() {
        /*
         * String destination, 目的地（队列名或者交换机名字）
         * DestinationType destinationType, 目的地类型（Queue、Exhcange）
         * String exchange,
         * String routingKey,
         * Map<String, Object> arguments
         * */
        return new Binding("ware.delay.queue",
                Binding.DestinationType.QUEUE,
                "ware-event-exchange",
                "ware.create.ware",
                null);
    }

    @Bean
    public Binding orderReleaseBinding() {

        return new Binding("ware.release.ware.queue",
                Binding.DestinationType.QUEUE,
                "ware-event-exchange",
                "ware.release.ware",
                null);
    }


}
