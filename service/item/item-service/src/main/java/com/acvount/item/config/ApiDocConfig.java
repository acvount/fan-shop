package com.acvount.item.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author : acfan
 * date : create in 2023/7/12 11:49
 * description :
 **/

@Configuration
public class ApiDocConfig {


    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("物品服务API")
                        .description("物品服务所有API")
                        .version("v1"));
//                .externalDocs(new ExternalDocumentation()
//                        .description("外部文档")
//                        .url("https://springshop.wiki.github.org/docs"));
    }
}
