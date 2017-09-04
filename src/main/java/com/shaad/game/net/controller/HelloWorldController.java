package com.shaad.game.net.controller;

import com.shaad.game.net.Request;
import com.shaad.game.net.response.HelloWorldResponse;
import com.shaad.game.net.response.Response;

import static com.shaad.game.net.HttpMethod.GET;


public class HelloWorldController extends ControllerBase {
    public HelloWorldController() {
        super("/hello", GET);
    }

    @Override
    public Response handle(Request request) {
        return new HelloWorldResponse("SHaaD");
    }
}
