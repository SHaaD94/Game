package com.shaad.game.controller.user;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.user.UserOfficeResponse;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.service.FighterService;
import com.shaad.game.service.UserService;

import java.util.UUID;

import static com.shaad.game.service.Constants.USER_ID;

public class UserOfficeController extends ControllerBase {
    private final SessionManager sessionManager;
    private final UserService userService;
    private final FighterService fighterService;

    public UserOfficeController(SessionManager sessionManager,
                                UserService userService,
                                FighterService fighterService) {
        super("/office", HttpMethod.GET);
        this.sessionManager = sessionManager;
        this.userService = userService;
        this.fighterService = fighterService;
    }

    @Override
    public Response handle(Request request) {
        UUID uuid = sessionManager.parseSessionId(request);
        if (uuid == null) {
            return new RedirectResponse("/login");
        }
        Long userId = sessionManager.getValueFromSession(uuid, USER_ID, Long.class);
        if (userId == null) {
            return new RedirectResponse("/login");
        }

        return new UserOfficeResponse(userService.findUser(userId), fighterService.findFighter(userId));
    }
}