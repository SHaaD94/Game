package com.shaad.game.controller.duel;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.domain.Duel;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.duel.DuelResponse;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.service.DuelService;
import com.shaad.game.service.FighterService;
import com.shaad.game.service.UserService;

import java.util.UUID;

import static com.shaad.game.service.Constants.USER_ID;

public class DuelController extends ControllerBase {
    private final SessionManager sessionManager;
    private final DuelService duelService;
    private final UserService userService;
    private final FighterService fighterService;

    public DuelController(SessionManager sessionManager,
                          DuelService duelService,
                          UserService userService,
                          FighterService fighterService) {
        super("/duel", HttpMethod.GET);
        this.sessionManager = sessionManager;
        this.duelService = duelService;
        this.userService = userService;
        this.fighterService = fighterService;
    }


    @Override
    public Response handle(Request request) {
        UUID uuid = sessionManager.parseSessionId(request);
        if (uuid == null) {
            return new RedirectResponse("/login");
        }
        Long userId = sessionManager.getValueFromSession(uuid, USER_ID, Long.class);
        if (userId == null) {
            return new RedirectResponse("/login");
        }

        Duel duel = duelService.getDuelByUserId(userId);
        if (duel == null) {
            return new RedirectResponse("/office");
        }

        return new DuelResponse(duel,
                userId,
                userService.findUser(userId).getLogin(),
                duel.getYourFighter(userId).getHealth(),
                duel.getYourFighter(userId).getDamage(),
                duel.getOpponentUserId(userId),
                userService.findUser(duel.getOpponentUserId(userId)).getLogin(),
                duel.getOpponentFighter(userId).getHealth(),
                duel.getOpponentFighter(userId).getDamage(),
                duel.aggregateLogs());
    }
}
