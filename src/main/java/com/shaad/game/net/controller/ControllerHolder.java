package com.shaad.game.net.controller;

import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.NotFoundResponse;
import com.shaad.game.net.response.Response;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class ControllerHolder {
    private final Map<String, Map<HttpMethod, Controller>> controllers = new HashMap<>();

    public ControllerHolder(List<Controller> controllersList) {
        controllersList.forEach(x -> {
            Map<HttpMethod, Controller> method2Controllers =
                    controllers.computeIfAbsent(x.getPath(), s -> new EnumMap<>(HttpMethod.class));

            checkState(!method2Controllers.containsKey(x.getMethod()),
                    "Controller for path {} and method {} already exists", x.getPath(), x.getMethod().toString());

            method2Controllers.put(x.getMethod(), x);
        });
    }

    public Response handleRequest(Request request) {
        Map<HttpMethod, Controller> potentialControllers = controllers.get(request.getPath());
        if (potentialControllers == null) {
            return new NotFoundResponse();
        }

        Controller controller = potentialControllers.get(request.getMethod());
        if (controller == null) {
            return new NotFoundResponse();
        }

        return controller.handle(request);
    }
}
