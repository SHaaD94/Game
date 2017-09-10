package com.shaad.game.controller.user;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.SessionManager;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.user.UserOfficeResponse;
import com.shaad.game.service.UserService;

import java.util.UUID;

public class UserOfficeController extends ControllerBase {
    private final SessionManager sessionManager;
    private final UserService userService;

    public UserOfficeController(UserService userService, SessionManager sessionManager) {
        super("/office", HttpMethod.GET);
        this.sessionManager = sessionManager;
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        UUID uuid = sessionManager.parseSessionId(request);
        if (uuid == null) {
            return new RedirectResponse("/login");
        }
        Long userId = sessionManager.getUserByToken(uuid);
        if (userId == null) {
            return new RedirectResponse("/login");
        }
        return new UserOfficeResponse(userService.findUser(userId));
    }
}