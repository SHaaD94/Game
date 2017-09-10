package com.shaad.game;

import com.shaad.game.controller.ControllerHolder;
import com.shaad.game.controller.MainScreenController;
import com.shaad.game.controller.duel.AttackActionController;
import com.shaad.game.controller.duel.DuelController;
import com.shaad.game.controller.duel.FindOpponentController;
import com.shaad.game.controller.duel.LastDuelController;
import com.shaad.game.controller.user.AuthorizationController;
import com.shaad.game.controller.user.LoginController;
import com.shaad.game.controller.user.LogoutController;
import com.shaad.game.controller.user.UserOfficeController;
import com.shaad.game.net.Server;
import com.shaad.game.net.decoder.CommonResponseDecoder;
import com.shaad.game.net.session.SessionCleaner;
import com.shaad.game.net.session.SessionManager;
import com.shaad.game.repository.DuelRepository;
import com.shaad.game.repository.FighterRepository;
import com.shaad.game.repository.UserRepository;
import com.shaad.game.service.DuelService;
import com.shaad.game.service.FighterService;
import com.shaad.game.service.UserService;

public class App {
    public static void main(String[] args) {
        SessionManager sessionManager = new SessionManager();
        new SessionCleaner(sessionManager).run();

        DuelRepository duelRepository = new DuelRepository();
        UserRepository userRepository = new UserRepository();
        FighterRepository fighterRepository = new FighterRepository();

        FighterService fighterService = new FighterService(fighterRepository);
        UserService userService = new UserService(userRepository, fighterRepository);
        DuelService duelService = new DuelService(duelRepository, fighterService);

        try (Server server = new Server(8080,
                new CommonResponseDecoder(),
                new ControllerHolder(
                        new MainScreenController(),

                        new LoginController(sessionManager),
                        new AuthorizationController(userService, sessionManager),
                        new LogoutController(sessionManager, duelService),

                        new UserOfficeController(sessionManager, userService, fighterService),

                        new FindOpponentController(sessionManager, duelService),
                        new DuelController(sessionManager, duelService, userService),
                        new AttackActionController(sessionManager, duelService),
                        new LastDuelController(sessionManager, duelRepository, userService)
                )
        )) {
            server.run();
        }
    }
}
