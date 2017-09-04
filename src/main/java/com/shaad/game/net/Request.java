package com.shaad.game.net;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Request {
    private final HttpMethod method;
    private final String path;
    private final List<String> headers;
    private final Map<String, String> params;
    private final String body;
}
