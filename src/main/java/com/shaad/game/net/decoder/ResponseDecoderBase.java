package com.shaad.game.net.decoder;

import com.shaad.game.net.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
abstract class ResponseDecoderBase implements ResponseDecoder {
    private static final String STATUS_LINE_TEMPLATE = "HTTP/1.1 %d %s\n";
    private static final String MANDATORY_HEADERS =
            "Server: Awesome-Server\n" +
                    "Connection: Closed\n";

    private final String contentType;

    ResponseDecoderBase(String contentType) {
        this.contentType = contentType;
    }

    public String decode(Response response) {
        String statusLine = String.format(STATUS_LINE_TEMPLATE, response.getStatus().getCode(), response.getStatus().name());
        String responseDecoded = statusLine +
                MANDATORY_HEADERS +
                "Content-Type: " + contentType + "; charset=UTF-8\n";

        for (String header : response.getHeaders()) {
            if (!header.endsWith("\n")) {
                header = header + "\n";
            }
            responseDecoded = responseDecoded.concat(header);
        }

        responseDecoded += "\n";

        if (response.getBody() != null) {
            responseDecoded += decodeBody(response);
        }

        return responseDecoded;
    }

    protected String decodeBody(Response response) {
        return response.getBody().toString();
    }

}
