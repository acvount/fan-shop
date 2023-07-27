package com.acvount.server.log.handle.stage;

import com.acvount.server.log.dto.LogMessage;
import io.micrometer.common.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : acfan
 * date : create in 2023/7/25 21:46
 * description :
 **/

public interface LogStage {
    void consumer(LogMessage logMessage);

    default List<String> splitMessage(String content) {
        return Stream.of(content.replaceAll("\\u0028", "(")
                        .replaceAll("\\u0029", ")")
                        .toLowerCase().split("\n"))
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }
}
