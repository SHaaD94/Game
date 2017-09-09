package com.shaad.game.net.decoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class HtmlResponseDecoder extends ResponseDecoderBase {

    HtmlResponseDecoder() {
        super("text/html");
    }
}
