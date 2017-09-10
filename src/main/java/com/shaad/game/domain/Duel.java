package com.shaad.game.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Duel {
    private final long id;
    private final long firstUserId;
    private final long secondUserId;
    private final List<String> logLines = new ArrayList<>();
    private final Date creationDate;
    private DuelStatus status;

    private FighterState fighter1;
    private FighterState fighter2;

    public Duel(long id, long firstUserId, long secondUserId, Date creationDate, DuelStatus status) {
        this.id = id;
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.creationDate = creationDate;
        this.status = status;
    }

    public void addLogLine(String line) {
        logLines.add(line);
    }

    public Long getOpponentUserId(long yourUserId) {
        return firstUserId == yourUserId ? secondUserId : firstUserId;
    }

    public FighterState getOpponentFighter(long yourUserId) {
        return firstUserId == yourUserId ? fighter2 : fighter1;
    }

    public FighterState getYourFighter(long yourUserId) {
        return firstUserId != yourUserId ? fighter2 : fighter1;
    }

    public String aggregateLogs() {
        StringJoiner joiner = new StringJoiner("\n");
        logLines.forEach(joiner::add);
        return joiner.toString();
    }
}
