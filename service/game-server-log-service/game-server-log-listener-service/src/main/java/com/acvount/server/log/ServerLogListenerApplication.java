package com.acvount.server.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : acfan
 * date : create in 2023/7/16 18:30
 * description : 服务日志监听程序
 **/

@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class ServerLogListenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerLogListenerApplication.class, args);
    }
}
