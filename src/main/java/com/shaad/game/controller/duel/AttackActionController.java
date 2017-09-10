package com.shaad.game.controller.duel;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.domain.Duel;
import com.shaad.game.domain.DuelStatus;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.service.DuelService;
import com.shaad.game.service.FighterService;
import com.shaad.game.service.UserService;

import java.util.UUID;

import static com.shaad.game.service.Constants.USER_ID;

public class AttackActionController extends ControllerBase {
    private final SessionManager sessionManager;
    private final DuelService duelService;

    public AttackActionController(SessionManager sessionManager,
                                  DuelService duelService){
        super("/attackAction", HttpMethod.POST);
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
        if (duel == null) {
            return new RedirectResponse("/office");
        }

        duel = duelService.handleAttack(userId);
        if (duel == null || duel.getStatus() == DuelStatus.FINISHED) {
            return new RedirectResponse("/lastDuel");
        }

        return new RedirectResponse("/duel");
    }
}
