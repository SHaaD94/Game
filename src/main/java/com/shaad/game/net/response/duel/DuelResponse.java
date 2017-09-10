package com.shaad.game.net.response.duel;

import com.shaad.game.domain.Duel;
import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;

import java.util.LinkedHashMap;

public class DuelResponse extends HtmlResponse {
    private static final String pageTemplate = readPageTemplateFromResources("duel_screen");

    //fixme remove duel from here
    public DuelResponse(Duel duel,
                        Long firstUserId,
                        String user1Login,
                        int hp1,
                        int dmg1,
                        Long secondUserId,
                        String user2Login,
                        int hp2,
                        int dmg2,
                        String logs) {
        super(HttpStatus.OK, new LinkedHashMap<String, String>() {{
            put("duel_id", Long.toString(duel.getId()));
            put("status", duel.getStatus().toString());
            put("login1", user1Login);
            put("login2", user2Login);
            put("damage1", Integer.toString(dmg1));
            put("damage2", Integer.toString(dmg2));
            put("hp1", Integer.toString(hp1));
            put("hp2", Integer.toString(hp2));
            put("logs", logs.replaceAll("\n", "<br>"));
            put(firstUserId.toString(), user1Login);
            put(secondUserId.toString(), user2Login);

        }});
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
