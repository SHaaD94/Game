package com.shaad.game.net.response;

import com.shaad.game.net.HttpStatus;
import lombok.Getter;

@Getter
public class MainScreenResponse extends HtmlResponse {
    private static final String pageTemplate = readPageTemplateFromResources("main_screen");

    public MainScreenResponse() {
        super(HttpStatus.OK);
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
