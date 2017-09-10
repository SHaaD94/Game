package com.shaad.game.controller.user;

import com.google.common.base.Strings;
import com.shaad.game.controller.ControllerBase;
import com.shaad.game.exception.WrongUserPasswordException;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.user.LoginResponse;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.service.UserService;

import java.util.UUID;

import static com.shaad.game.net.HttpMethod.POST;
import static com.shaad.game.service.Constants.*;
import static com.shaad.game.util.UrlDecodeUtil.decode;


public class AuthorizationController extends ControllerBase {
    private final UserService userService;
    private final SessionManager sessionManager;

    public AuthorizationController(UserService userService, SessionManager sessionManager) {
        super("/login", POST);
        this.userService = userService;
        this.sessionManager = sessionManager;
    }

    @Override
    public Response handle(Request request) {
        String body = decode(request.getBody());
        if (Strings.isNullOrEmpty(body)) {
            return new LoginResponse("Login and password should not be empty");
        }

        String[] bodyParams = body.split("&");
        if (bodyParams.length != 2) {
            return new LoginResponse("Login and password should not be empty");
        }

        String login = null;
        String password = null;
        for (String bodyParam : bodyParams) {
            if (bodyParam.startsWith("login")) {
                login = bodyParam.replace("login=", "");
            }
            if (bodyParam.startsWith("password")) {
                password = bodyParam.replace("password=", "");
            }
        }

        if (Strings.isNullOrEmpty(login) || Strings.isNullOrEmpty(password)) {
            return new LoginResponse("Login and password should not be empty");
        }

        Long userId;
        try {
            userId = userService.createOrGetUser(login, password);
        } catch (WrongUserPasswordException e) {
            return new LoginResponse(e.getMessage());
        }

        UUID sessionUUID = sessionManager.createSession();
        sessionManager.setSessionValue(sessionUUID, USER_ID, userId);
        sessionManager.setSessionValue(sessionUUID, USER_LOGIN, login);
        RedirectResponse redirectResponse = new RedirectResponse("/office");
        redirectResponse.setCookie(SESSION_ID, sessionUUID.toString());
        return redirectResponse;
    }
}
