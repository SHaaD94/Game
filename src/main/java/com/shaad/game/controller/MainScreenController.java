package com.shaad.game.controller;

import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.MainScreenResponse;
import com.shaad.game.net.response.Response;

public class MainScreenController extends ControllerBase {
    public MainScreenController() {
        super("/", HttpMethod.GET);
    }

    @Override
    public Response handle(Request request) {
        return new MainScreenResponse();
    }
}
