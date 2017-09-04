package com.shaad.game.net.decoder;

import com.shaad.game.net.response.Response;

public interface ResponseDecoder {
    String decode(Response response);
}
