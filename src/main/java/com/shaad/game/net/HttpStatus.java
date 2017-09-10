package com.shaad.game.net;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HttpStatus {
    OK(200),
    REDIRECT(302),
    BAD_REQUEST(400),
    NON_AUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_ERROR(500);

    private final int code;
}
