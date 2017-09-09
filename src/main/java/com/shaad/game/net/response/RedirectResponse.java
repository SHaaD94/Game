package com.shaad.game.net.response;

import com.shaad.game.net.HttpStatus;

public class RedirectResponse extends HtmlResponse {
    private static final String redirectTemplate = "Refresh: 0; url=%s";

    public RedirectResponse(String redirectPath) {
        super(HttpStatus.OK);

        headers.add(String.format(redirectTemplate, redirectPath));
    }

    @Override
    protected String getPageTemplate() {
        return "<html></html>";
    }
}
