package com.hyb.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "com.hyb")
@EnableDiscoveryClient
public class ServiceVideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVideoApplication.class,args);
    }
}
