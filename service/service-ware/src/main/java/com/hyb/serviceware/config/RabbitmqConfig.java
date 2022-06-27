package com.hyb.serviceware.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RabbitmqConfig {

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
