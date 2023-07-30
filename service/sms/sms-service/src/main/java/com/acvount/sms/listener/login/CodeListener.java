package com.acvount.sms.listener.login;

import com.acvount.sms.AliSmsService;
import com.acvount.sms.code.bean.SendPhoneCodeSmsDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author : acfan
 * date : create in 2023/7/2 21:06
 * description :
 **/

@Configuration
@Slf4j
public class CodeListener {

    @Resource
    private AliSmsService aliSmsService;

    @Bean
    public Function<Flux<Message<SendPhoneCodeSmsDTO>>, Mono<Void>> phoneCodeSmsConsumer() {
        return flux -> flux.map(message -> {
            aliSmsService.sendSMS(message.getPayload());
            return message;
        }).then();
    }

    @SuppressWarnings("unused")
    public void error(Message<?> message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        log.error("Handling ERROR, errorMessage = {} ", errorMessage);
    }
}
