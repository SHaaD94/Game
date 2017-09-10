package com.shaad.game.net.response.duel;

import com.shaad.game.domain.Duel;
import com.shaad.game.domain.User;
import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;

import java.util.HashMap;

public class DuelResponse extends HtmlResponse {
    private static final String pageTemplate = readPageTemplateFromResources("duel_screen");

    public DuelResponse(Duel duel, User user1, User user2) {
        super(HttpStatus.OK, new HashMap<String, String>() {{
            put("duel_id", Long.toString(duel.getId()));
            put("status", duel.getStatus().toString());
            put("login1", user1.getLogin());
            put("login2", user2.getLogin());
        }});
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
