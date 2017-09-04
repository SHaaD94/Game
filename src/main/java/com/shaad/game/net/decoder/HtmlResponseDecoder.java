package com.shaad.game.net.decoder;

import com.shaad.game.net.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HtmlResponseDecoder implements ResponseDecoder {
    public String decode(Response response) {
        String responseDecoded = "HTTP/1.1 " + response.getStatus().getCode() + " " + response.getStatus().name() + "\n" +
                "Server: Awesome-Server\n" +
                "Connection: Closed\n" +
                "Content-Type: text/html; charset=UTF-8\n" +
                "\n";

        responseDecoded += response.getBody();

        return responseDecoded;
    }
}
