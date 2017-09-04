package com.shaad.game.net.decoder;

import com.shaad.game.net.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

@Slf4j
public class JsonResponseDecoder implements ResponseDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String decode(Response response) {
        try {
            String responseDecoded = "HTTP/1.1 " + response.getStatus().getCode() + " " + response.getStatus().name() + "\n" +
                    "Server: Awesome-Server\n" +
                    "Connection: Closed\n" +
                    "Content-Type: application/json; charset=UTF-8\n" +
                    "\n";
            if (response.getBody() != null) {
                responseDecoded += objectMapper.writeValueAsString(response.getBody());
            }
            return responseDecoded;
        } catch (IOException e) {
            log.error("Failed to decode response:{}", response, e);
            throw new RuntimeException(e);
        }
    }
}
