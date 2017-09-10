package com.shaad.game.net.response;

import com.shaad.game.net.HttpStatus;

public class RedirectResponse extends HtmlResponse {
    private static final String redirectTemplate = "Location: %s";

    public RedirectResponse(String redirectPath) {
        super(HttpStatus.REDIRECT);

        headers.add(String.format(redirectTemplate, redirectPath));
    }

    @Override
    protected String getPageTemplate() {
        return "";
    }
}
