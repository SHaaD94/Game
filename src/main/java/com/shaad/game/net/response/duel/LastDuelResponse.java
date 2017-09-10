package com.shaad.game.net.response.duel;

import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;

import java.util.LinkedHashMap;

public class LastDuelResponse extends HtmlResponse {
    private static final String pageTemplate = readPageTemplateFromResources("last_duel");

    public LastDuelResponse(Long user1Id,
                            Long user2Id,
                            String user1Login,
                            String user2Login,
                            String logs) {
        super(HttpStatus.OK, new LinkedHashMap<String, String>() {{
            put("login1", user1Login);
            put("login2", user2Login);
            put("logs", logs.replaceAll("\n", "<br>"));
            put(user1Id.toString(), user1Login);
            put(user2Id.toString(), user2Login);
        }});
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
