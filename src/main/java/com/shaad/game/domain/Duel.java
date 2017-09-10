package com.shaad.game.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Duel {
    private final long id;
    private final long firstUserId;
    private final long secondUserId;
    private final DuelStatus status;
    private final List<String> logLines = new ArrayList<>();

    public void addLogLine(String line) {
        logLines.add(line);
    }
}
