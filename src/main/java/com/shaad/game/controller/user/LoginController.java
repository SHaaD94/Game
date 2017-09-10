package com.shaad.game.controller.user;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.net.Request;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.user.LoginResponse;

import java.util.UUID;

import static com.shaad.game.net.HttpMethod.GET;
import static com.shaad.game.service.Constants.USER_ID;


public class LoginController extends ControllerBase {
    private final SessionManager sessionManager;

    public LoginController(SessionManager sessionManager) {
        super("/login", GET);
        this.sessionManager = sessionManager;
    }

    @Override
    public Response handle(Request request) {
        UUID uuid = sessionManager.parseSessionId(request);

        if (uuid != null) {
            if (sessionManager.getValueFromSession(uuid, USER_ID, Long.class) != null) {
                return new RedirectResponse("/office");
            }
        }

        return new LoginResponse();
    }
}
