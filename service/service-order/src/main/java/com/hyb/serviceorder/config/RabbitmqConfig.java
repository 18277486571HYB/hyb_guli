package com.hyb.serviceorder.config;

import com.rabbitmq.client.ConnectionFactory;
import net.bytebuddy.asm.Advice;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitmqConfig {

    @Autowired
    RabbitTemplate template;


    @PostConstruct
    public void init(){
        /*
         * 相当于在该配置类调用构造器初始化对象后调用
         * 这是Broker的确认机制,成功后才会回调
         * */
        template.setConfirmCallback((correlationData, b, s) -> {
//                correlationData 当前消息关联的数据
            System.out.println("broker确认回调机制");
        });
        /*
        * 只有未到达的消息才会触发
        * */
        template.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {

            }
        });
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /*
    * 使用json格式化进行消息转化
    * */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
