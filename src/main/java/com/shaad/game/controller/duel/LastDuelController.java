package com.shaad.game.controller.duel;

import com.shaad.game.controller.ControllerBase;
import com.shaad.game.domain.Duel;
import com.shaad.game.net.HttpMethod;
import com.shaad.game.net.Request;
import com.shaad.game.net.response.RedirectResponse;
import com.shaad.game.net.response.Response;
import com.shaad.game.net.response.duel.LastDuelResponse;
import com.shaad.game.net.response.duel.NoLastDuelResponse;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.repository.DuelRepository;
import com.shaad.game.service.UserService;

import java.util.UUID;

import static com.shaad.game.service.Constants.USER_ID;

public class LastDuelController extends ControllerBase {
    private final SessionManager sessionManager;
    private final DuelRepository duelRepository;
    private final UserService userService;

    public LastDuelController(SessionManager sessionManager,
                              DuelRepository duelRepository,
                              UserService userService) {
        super("/lastDuel", HttpMethod.GET);
        this.sessionManager = sessionManager;
        this.duelRepository = duelRepository;
        this.userService = userService;
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

        Duel lastDuel = duelRepository.getLastFinishedDuel(userId);

        if (lastDuel == null) {
            return new NoLastDuelResponse();
        }

        return new LastDuelResponse(
                userId,
                lastDuel.getOpponentUserId(userId),
                userService.findUser(userId).getLogin(),
                userService.findUser(lastDuel.getOpponentUserId(userId)).getLogin(),
                lastDuel.aggregateLogs());
    }
}
