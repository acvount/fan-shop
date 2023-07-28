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

    private final Map<String, LogStage> stageMap;

    @Autowired
    public LogTypeStageChecker(AdminStage adminStage, ChatStage chatStage, EconomyStage economyStage,
                               EventKillStage eventKillStage, FamepointsStage famepointsStage,
                               GamePlayStage gamePlayStage, KillStage killStage, LoginStage loginStage,
                               ViolationsStage violationsStage) {
        stageMap = Map.of("admin", adminStage, "chat", chatStage, "economy", economyStage,
                "event_kill", eventKillStage, "famepoints", famepointsStage,
                "gameplay", gamePlayStage, "kill", killStage, "login", loginStage,
                "violations", violationsStage);
    }

    public LogStage getStage(String type) {
        return stageMap.get(type);
    }
}
