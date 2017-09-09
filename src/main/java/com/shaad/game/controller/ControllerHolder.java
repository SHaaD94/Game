package com.shaad.game.controller;

import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.errors.InternalErrorResponse;
import com.shaad.game.net.response.errors.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
public class ControllerHolder {
    private final Map<String, Map<HttpMethod, Controller>> path2controllers = new HashMap<>();

    public ControllerHolder(Controller... controller) {
        Arrays.stream(controller).forEach(x -> {
            Map<HttpMethod, Controller> method2Controllers =
                    path2controllers.computeIfAbsent(x.getPath(), s -> new EnumMap<>(HttpMethod.class));

            checkState(!method2Controllers.containsKey(x.getMethod()),
                    "Controller for path {} and method {} already exists", x.getPath(), x.getMethod().toString());

            method2Controllers.put(x.getMethod(), x);
        });
    }

    public Response handleRequest(Request request) {
        Map<HttpMethod, Controller> potentialControllers = path2controllers.get(request.getPath());
        if (potentialControllers == null) {
            return new NotFoundResponse();
        }

        Controller controller = potentialControllers.get(request.getMethod());
        if (controller == null) {
            return new NotFoundResponse();
        }

        try {
            return controller.handle(request);
        } catch (Exception e) {
            log.error("Something went wrong", e);
            return new InternalErrorResponse(e.getMessage());
        }
    }
}
