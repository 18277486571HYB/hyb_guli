package com.hyb.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
@EnableCaching
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //排除数据源配置,因为oss不需要数据源,如果不排除会报错
@ComponentScan(basePackages = "com.hyb") //这里是扫描共同模块下的com.hyb,不是扫描OssApplication下的com.hyb
public class OssApplication {
    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class,args);
    }
}
