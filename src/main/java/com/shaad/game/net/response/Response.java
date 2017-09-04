package com.shaad.game.net.response;

import com.shaad.game.net.HttpStatus;

import java.util.List;

public interface Response {
    HttpStatus getStatus();

    List<String> getHeaders();

    Object getBody();
}
