package com.shaad.game.net.decoder;

import com.shaad.game.net.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@Slf4j
class JsonResponseDecoder extends ResponseDecoderBase {
    private final ObjectMapper objectMapper = new ObjectMapper();

    JsonResponseDecoder() {
        super("application/json");
    }

    @Override
    protected String decodeBody(Response response) {
        try {
            return objectMapper.writeValueAsString(response.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
