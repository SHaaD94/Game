package com.shaad.game.domain;

import lombok.Data;

@Data
public class User {
    private final long id;
    private final String login;
    private final String passwordHash;
}
