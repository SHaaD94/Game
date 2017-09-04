package com.shaad.game.net.controller;

import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.Response;

public interface Controller {
    String getPath();

    HttpMethod getMethod();

    Response handle(Request request);
}
