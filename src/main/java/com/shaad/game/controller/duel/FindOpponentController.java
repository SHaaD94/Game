package com.shaad.game.controller.duel;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.domain.Duel;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.FindOpponentResponse;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.service.DuelService;

import java.util.UUID;

import static com.shaad.game.service.Constants.OPPONENT_SEARCH_START_DATE;
import static com.shaad.game.service.Constants.USER_ID;

public class FindOpponentController extends ControllerBase {
    private final SessionManager sessionManager;
    private final DuelService duelService;

    public FindOpponentController(SessionManager sessionManager, DuelService duelService) {
        super("/findOpponent", HttpMethod.GET);
        this.sessionManager = sessionManager;
        this.duelService = duelService;
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
        if (duel != null) {
            sessionManager.removeSessionValue(uuid, OPPONENT_SEARCH_START_DATE);
            return new RedirectResponse("/duel");
        }

        Long searchStartDate = sessionManager.getValueFromSession(uuid, OPPONENT_SEARCH_START_DATE, Long.class);
        if (searchStartDate == null) {
            duelService.registerInQueue(userId);
            sessionManager.setSessionValue(uuid, OPPONENT_SEARCH_START_DATE, System.currentTimeMillis());
            return new FindOpponentResponse(0);
        }

        return new FindOpponentResponse((System.currentTimeMillis() - searchStartDate) / 1000);
    }
}
