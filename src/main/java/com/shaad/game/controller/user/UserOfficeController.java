package com.shaad.game.controller.user;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.domain.User;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.SessionManager;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.user.UserOfficeResponse;
import com.shaad.game.service.UserService;

import java.util.Optional;
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
        Optional<String> sessionIdCookie = request.getHeaders().stream().filter(x -> x.startsWith("Cookie")).filter(x -> x.contains("SessionId"))
                .findFirst();

        if (sessionIdCookie.isPresent()) {
            String cookie = sessionIdCookie.get();
            String sessionUUID = cookie.replaceAll(".*SessionId=", "");
            Long userId = sessionManager.getUserByToken(UUID.fromString(sessionUUID));
            if (userId == null) {
                return new RedirectResponse("/signup");
            }
            User user = userService.findUser(userId);
            return new UserOfficeResponse(user.getLogin());
        } else {
            return new RedirectResponse("/signup");
        }
    }
}
