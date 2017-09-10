package com.shaad.game.domain;

import lombok.Data;

@Data
public class Fighter {
    private final long id;
    private final int rating;
    private final int health;
    private final int damage;
}
