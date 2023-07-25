package com.acvount.server.log.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author : acfan
 * date : create in 2023/7/22 11:28
 * description :
 **/

//
@Configuration
@RefreshScope
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private Integer port;
    @Value("${spring.data.redis.password}")
    private String password;


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = "redis://" + host + ":" + port;
        //使用json序列化方式
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer().setAddress(address).setPassword(password);

        return Redisson.create(config);
    }

}
