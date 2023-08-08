package com.acvount.server.log.handle;

import com.acvount.server.log.dto.LogMessage;
import com.acvount.server.log.handle.stage.LogStage;
import com.acvount.server.log.handle.stage.LogTypeStageChecker;
import com.acvount.server.log.lose.service.LoseLogService;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
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
    @Resource
    private LoseLogService loseLogService;

    @Bean
    public Function<Flux<Message<String>>, Mono<Void>> logMessageConsumer() {
        return flux -> flux.map(message -> {
            try {
                LogMessage logMessage = JSONObject.parseObject(message.getPayload(), LogMessage.class);
                LogStage stage = logTypeStageChecker.getStage(logMessage.getType());
                if (Objects.isNull(stage)) {
                    log.error("no stage : {}", logMessage);
                    return message;
                }
                stage.consumer(logMessage);
            } catch (Exception e) {
                loseLogService.addLoseLog(111L, message.getPayload(), "handle-message:" + e.getMessage());
            }
            return message;
        }).then();
    }
}
