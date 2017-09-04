package com.shaad.game.net.response;

import com.shaad.game.net.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class NotFoundResponse extends HtmlResponse {

    private static final String pageTemplate = readPageTemplateFromResources("not_found");

    public NotFoundResponse() {
        super(HttpStatus.NOT_FOUND, new HashMap<>());
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
