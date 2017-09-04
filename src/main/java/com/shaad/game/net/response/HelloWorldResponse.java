package com.shaad.game.net.response;

import com.shaad.game.net.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class HelloWorldResponse extends HtmlResponse {

    private static final String pageTemplate = readPageTemplateFromResources("hello_user");

    public HelloWorldResponse(String user) {
        super(HttpStatus.OK, new HashMap<String, String>() {{
            put("user", user);
        }});
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
