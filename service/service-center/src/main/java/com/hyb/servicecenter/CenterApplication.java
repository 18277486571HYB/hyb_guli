package com.hyb.servicecenter;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hyb")
@MapperScan("com.hyb.servicecenter.mapper")
@EnableDiscoveryClient
@EnableFeignClients

public class CenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(CenterApplication.class,args);
    }
}
