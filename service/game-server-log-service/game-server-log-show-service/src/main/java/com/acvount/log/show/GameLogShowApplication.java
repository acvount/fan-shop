package com.acvount.log.show;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author : acfan
 * date : create in 2023/7/29 15:56
 * description :
 **/

@EnableDiscoveryClient
@SpringBootApplication
public class GameLogShowApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(GameLogShowApplication.class).run(args);

    }
}
