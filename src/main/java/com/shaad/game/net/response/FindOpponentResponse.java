package com.shaad.game.net.response;

import com.shaad.game.net.HttpStatus;

import java.util.HashMap;

public class FindOpponentResponse extends HtmlResponse {
    private static final String pageTemplate = readPageTemplateFromResources("find_opponent");

    public FindOpponentResponse(long searchTime) {
        super(HttpStatus.OK, new HashMap<String, String>() {{
            put("seconds", Long.toString(searchTime));
        }});
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
