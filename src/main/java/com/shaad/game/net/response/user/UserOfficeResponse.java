package com.shaad.game.net.response.user;

import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class UserOfficeResponse extends HtmlResponse {

    private static final String pageTemplate = readPageTemplateFromResources("user_office");

    public UserOfficeResponse(String login) {
        super(HttpStatus.OK, new HashMap<String, String>() {{
            put("user", login);
        }});
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
