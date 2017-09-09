package com.shaad.game.controller.user;

import com.shaad.game.net.Request;
import com.shaad.game.controller.ControllerBase;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.user.SignupResponse;

import static com.shaad.game.net.HttpMethod.GET;


public class SignupController extends ControllerBase {
    public SignupController() {
        super("/signup", GET);
    }

    @Override
    public Response handle(Request request) {
        return new SignupResponse();
    }
}
