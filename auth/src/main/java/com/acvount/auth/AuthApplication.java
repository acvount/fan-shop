package com.acvount.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author : acfan
 * date : create in 2023/6/28 21:30
 * description :
 **/

@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.acvount.auth.*.mapper")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
