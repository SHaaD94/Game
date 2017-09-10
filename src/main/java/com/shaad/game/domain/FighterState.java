package com.shaad.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FighterState {
    public FighterState(Fighter fighter) {
        this.health = fighter.getHealth();
        this.damage = fighter.getDamage();
    }

    private int health;
    private int damage;
}
