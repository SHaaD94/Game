package com.shaad.game.net.response.user;

import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class LoginResponse extends HtmlResponse {

    private static final String pageTemplate = readPageTemplateFromResources("sign_up");

    public LoginResponse(String error) {
        super(HttpStatus.OK, new HashMap<String, String>() {{
            put("error", error);
        }});
    }

    public LoginResponse() {
        this("");
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
