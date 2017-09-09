package com.shaad.game.net.response.errors;

import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class InternalErrorResponse extends HtmlResponse {

    private static final String pageTemplate = readPageTemplateFromResources("internal_error");

    public InternalErrorResponse(String error) {
        super(HttpStatus.INTERNAL_ERROR, new HashMap<String, String>() {{
            put("error", error);
        }});
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
