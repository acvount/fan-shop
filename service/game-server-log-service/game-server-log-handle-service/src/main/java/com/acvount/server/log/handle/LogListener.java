package com.acvount.server.log.handle;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.handle.stage.LogStage;
import com.acvount.server.log.handle.stage.LogTypeStageChecker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author : acfan
 * date : create in 2023/7/2 21:06
 * description :
 **/

@Configuration
@Slf4j
public class LogListener {

    @Resource
    private LogTypeStageChecker logTypeStageChecker;

    @Bean
    public Function<Flux<Message<LogMessage>>, Mono<Void>> phoneCodeSmsConsumer() {
        return flux -> flux.map(message -> {
            LogMessage logMessage = message.getPayload();
            LogStage stage = logTypeStageChecker.getStage(logMessage.getType());
            if (Objects.isNull(stage)) {
                log.error("no stage : {}", logMessage);
            }
            stage.consumer(logMessage);
            return message;
        }).then();
    }

    @SuppressWarnings("unused")
    public void error(Message<?> message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        log.error("Handling ERROR, errorMessage = {} ", errorMessage);
    }

}
