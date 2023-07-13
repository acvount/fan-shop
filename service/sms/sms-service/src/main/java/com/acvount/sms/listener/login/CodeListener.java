package com.acvount.sms.listener.login;

import com.acvount.sms.code.bean.SendPhoneCodeSmsDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author : acfan
 * date : create in 2023/7/2 21:06
 * description :
 **/

@Configuration
@Slf4j
public class CodeListener {
    @Bean
    public Function<Flux<Message<SendPhoneCodeSmsDTO>>, Mono<Void>> phoneCodeSmsConsumer() {
        return flux -> flux.map(message -> {

            System.out.println(message.getPayload());
            return message;
        }).then();
    }

    @SuppressWarnings("unused")
    public void error(Message<?> message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        log.error("Handling ERROR, errorMessage = {} ", errorMessage);
    }

}
