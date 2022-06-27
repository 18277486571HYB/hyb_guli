package com.hyb.serviceorder.config;

import com.hyb.serviceorder.entity.TOrder;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class OrderMqConfig {

//    @RabbitListener(queues = "order.release.order.queue")
//    public void listener(TOrder order, Channel channel, Message message) throws IOException {
//        System.out.println("确认接收消息"+order.toString());
//        //手动确认,哪个消息,是否批量保存
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//    }



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
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", 6000); // 消息过期时间 1分钟
        Queue queue = new Queue("order.delay.queue", true, false, false, arguments);

        return queue;
    }

    /**
     * 普通队列
     *
     * @return
     */
    @Bean
    public Queue orderReleaseQueue() {

        Queue queue = new Queue("order.release.order.queue", true, false, false);

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
        return new TopicExchange("order-event-exchange", true, false);

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
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
                null);
    }

    @Bean
    public Binding orderReleaseBinding() {

        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
    }


    /**
     * 订单释放直接和库存释放进行绑定
     * 有下面一个场景:
     * 如果订单释放延迟,这个时候库存释放到期,发现订单还未释放说明自己不用释放
     * 等到订单真正释放后,库存的响应早已经过去了,库存永远得不到释放
     * 所以我们可以让订单释放与库存释放直接进行绑定
     * 等订单释放,立马发送一个消息给订单的交换机,
     * 又因为订单交换机与库存释放队列进行绑定,所以这个又需要库存再去监听这个队列
     * @return
     */
    @Bean
    public Binding orderReleaseOtherBinding() {

        return new Binding("ware.release.ware.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.other.#",
                null);
    }

}
