package com.acvount.server.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author : acfan
 * date : create in 2023/7/25 21:43
 * description :
 **/

@SpringBootApplication
@EnableDiscoveryClient
public class ServerLogHandleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerLogHandleApplication.class, args);
    }
}
