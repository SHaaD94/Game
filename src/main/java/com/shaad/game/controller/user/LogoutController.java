package com.shaad.game.controller.user;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.SessionManager;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;

import java.util.UUID;

public class LogoutController extends ControllerBase {
    private final SessionManager sessionManager;

    public LogoutController(SessionManager sessionManager) {
        super("/logout", HttpMethod.GET);
        this.sessionManager = sessionManager;
    }

    @Override
    public Response handle(Request request) {
        UUID uuid = sessionManager.parseSessionId(request);
        if (uuid == null) {
            return new RedirectResponse("/login");
        }

        sessionManager.invalidateSession(uuid);
        return new RedirectResponse("/login");
    }
}