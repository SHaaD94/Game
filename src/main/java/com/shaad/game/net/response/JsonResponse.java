package com.shaad.game.net.response;

import com.shaad.game.net.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class JsonResponse implements Response {
    private final HttpStatus status;
    private final Object body;

    @Override
    public List<String> getHeaders() {
        return new ArrayList<>();
    }
}
