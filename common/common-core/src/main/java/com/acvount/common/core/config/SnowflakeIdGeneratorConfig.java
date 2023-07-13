package com.acvount.common.core.config;

import com.acvount.common.core.id.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author : acfan
 * date : create in 2023/7/1 18:37
 * description : 将雪花ID 算法注入到容器内。
 **/

@Configuration
public class SnowflakeIdGeneratorConfig {

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        long workerId = new Random().nextLong(0,SnowflakeIdGenerator.MAX_WORKER_ID);
        long dataCenterId = 1;
        return new SnowflakeIdGenerator(workerId, dataCenterId);
    }
}
