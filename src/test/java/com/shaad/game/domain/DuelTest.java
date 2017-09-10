package com.shaad.game.domain;

import org.junit.Assert;
import org.junit.Test;

public class DuelTest {
    @Test
    public void shouldReturnTrueResults() {
        Duel duel = new Duel(1, 1, 2, DuelStatus.FINISHED);

        FighterState firstFighterState = new FighterState(1, 1);
        FighterState secondFighterState = new FighterState(2, 2);
        duel.setFighter1(firstFighterState);
        duel.setFighter2(secondFighterState);

        Assert.assertEquals(duel.getYourFighter(1), firstFighterState);
        Assert.assertEquals(duel.getOpponentFighter(1), secondFighterState);
        Assert.assertEquals(duel.getOpponentUserId(1L), (Long) 2L);
    }

}