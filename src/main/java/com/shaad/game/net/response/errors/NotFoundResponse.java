package com.shaad.game.net.response.errors;

import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundResponse extends HtmlResponse {

    private static final String pageTemplate = readPageTemplateFromResources("not_found");

    public NotFoundResponse() {
        super(HttpStatus.NOT_FOUND);
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
