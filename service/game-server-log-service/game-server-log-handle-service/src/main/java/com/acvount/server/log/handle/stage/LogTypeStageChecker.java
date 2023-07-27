package com.acvount.server.log.handle.stage;

import com.acvount.server.log.handle.stage.stages.admin.AdminStage;
import com.acvount.server.log.handle.stage.stages.chat.ChatStage;
import com.acvount.server.log.handle.stage.stages.economy.EconomyStage;
import com.acvount.server.log.handle.stage.stages.event_kill.EventKillStage;
import com.acvount.server.log.handle.stage.stages.famepoints.FamepointsStage;
import com.acvount.server.log.handle.stage.stages.game_play.GamePlayStage;
import com.acvount.server.log.handle.stage.stages.kill.KillStage;
import com.acvount.server.log.handle.stage.stages.login.LoginStage;
import com.acvount.server.log.handle.stage.stages.violations.ViolationsStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class LogTypeStageChecker {

    private final Map<LogType, LogStage> stageMap;

    @Autowired
    public LogTypeStageChecker(AdminStage adminStage, ChatStage chatStage, EconomyStage economyStage,
                               EventKillStage eventKillStage, FamepointsStage famepointsStage,
                               GamePlayStage gamePlayStage, KillStage killStage, LoginStage loginStage,
                               ViolationsStage violationsStage) {
        stageMap = new EnumMap<>(LogType.class);
        stageMap.put(LogType.ADMIN, adminStage);
        stageMap.put(LogType.CHAT, chatStage);
        stageMap.put(LogType.ECONOMY, economyStage);
        stageMap.put(LogType.EVENT_KILL, eventKillStage);
        stageMap.put(LogType.FAMEPOINTS, famepointsStage);
        stageMap.put(LogType.GAMEPLAY, gamePlayStage);
        stageMap.put(LogType.KILL, killStage);
        stageMap.put(LogType.LOGIN, loginStage);
        stageMap.put(LogType.VIOLATIONS, violationsStage);
    }

    public LogStage getStage(String logType) {
        return stageMap.get(LogTypeStageChecker.LogType.valueOf(logType.toUpperCase()));
    }

    // 定义日志类型的枚举
    public enum LogType {
        ADMIN,
        CHAT,
        ECONOMY,
        EVENT_KILL,
        FAMEPOINTS,
        GAMEPLAY,
        KILL,
        LOGIN,
        VIOLATIONS;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
