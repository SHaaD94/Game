package com.shaad.game.controller.user;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.service.DuelService;

import java.util.UUID;

import static com.shaad.game.service.Constants.USER_ID;

public class LogoutController extends ControllerBase {
    private final SessionManager sessionManager;
    private final DuelService duelService;

    public LogoutController(SessionManager sessionManager, DuelService duelService) {
        super("/logout", HttpMethod.GET);
        this.sessionManager = sessionManager;
        this.duelService = duelService;
    }

    @Override
    public Response handle(Request request) {
        UUID uuid = sessionManager.parseSessionId(request);
        if (uuid == null) {
            return new RedirectResponse("/login");
        }

        Long userId = sessionManager.getValueFromSession(uuid, USER_ID, Long.class);
        if (userId != null) {
            duelService.unregister(userId);
        }

        sessionManager.invalidateSession(uuid);
        return new RedirectResponse("/login");
    }
}