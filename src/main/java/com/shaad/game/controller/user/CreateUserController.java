package com.shaad.game.controller.user;

import com.google.common.base.Strings;
import com.shaad.game.controller.ControllerBase;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.user.SignupResponse;
import com.shaad.game.net.response.user.UserOfficeResponse;
import com.shaad.game.service.UserService;

import static com.shaad.game.net.HttpMethod.POST;
import static com.shaad.game.util.UrlDecodeUtil.decode;


public class CreateUserController extends ControllerBase {
    private final UserService userService;

    public CreateUserController(UserService userService) {
        super("/signup", POST);
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        String body = decode(request.getBody());
        if (Strings.isNullOrEmpty(body)) {
            return new SignupResponse("Login and password should not be empty");
        }

        String[] bodyParams = body.split("&");
        if (bodyParams.length != 2) {
            return new SignupResponse("Login and password should not be empty");
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
            return new SignupResponse("Login and password should not be empty");
        }

        userService.createUser(login, password);

        return new UserOfficeResponse(login);
    }
}
