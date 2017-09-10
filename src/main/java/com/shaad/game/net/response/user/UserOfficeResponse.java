package com.shaad.game.net.response.user;

import com.shaad.game.domain.Fighter;
import com.shaad.game.domain.User;
import com.shaad.game.net.HttpStatus;
import com.shaad.game.net.response.HtmlResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class UserOfficeResponse extends HtmlResponse {

    private static final String pageTemplate = readPageTemplateFromResources("user_office");

    public UserOfficeResponse(User user, Fighter fighter) {
        super(HttpStatus.OK, new HashMap<String, String>() {{
            put("user", user.getLogin());
            put("rating", Integer.toString(fighter.getRating()));
            put("damage", Integer.toString(fighter.getDamage()));
            put("hp", Integer.toString(fighter.getHealth()));
        }});
    }

    @Override
    protected String getPageTemplate() {
        return pageTemplate;
    }
}
