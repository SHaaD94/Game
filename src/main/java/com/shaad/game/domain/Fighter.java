package com.shaad.game.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Fighter {
    private final long id;
    private int rating;
    private int health;
    private int damage;
}
