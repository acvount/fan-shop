package com.acvount.item;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author : acfan
 * date : create in 2023/7/16 12:19
 * description :
 **/

@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }
}
