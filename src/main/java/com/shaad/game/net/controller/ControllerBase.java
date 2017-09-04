package com.shaad.game.net.controller;

import com.shaad.game.net.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class ControllerBase implements Controller {
    protected final String path;
    protected final HttpMethod method;
}
