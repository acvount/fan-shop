package com.acvount.common.core.config;

import com.acvount.common.core.id.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : acfan
 * date : create in 2023/7/1 18:37
 * description : 将雪花ID 算法注入到容器内。
 **/

@Configuration
public class SnowflakeIdGeneratorConfig {

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        long workerId = 1;
        long dataCenterId = 1;
        return new SnowflakeIdGenerator(workerId, dataCenterId);
    }
}
