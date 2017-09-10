package com.shaad.game.net.response.duel;

import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;

public class NoLastDuelResponse extends HtmlResponse {
    private static final String pageTemplate = readPageTemplateFromResources("no_last_duel");

    public NoLastDuelResponse() {
        super(HttpStatus.OK);
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
