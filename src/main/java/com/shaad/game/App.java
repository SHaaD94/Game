package com.shaad.game;

import com.shaad.game.controller.ControllerHolder;
import com.shaad.game.controller.MainScreenController;
import com.shaad.game.controller.user.CreateUserController;
import com.shaad.game.controller.user.SignupController;
import com.shaad.game.net.Server;
import com.shaad.game.net.decoder.CommonResponseDecoder;
import com.shaad.game.repository.UserRepository;
import com.shaad.game.service.UserService;

public class App {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();

        UserService userService = new UserService(userRepository);

        try (Server server = new Server(8080,
                new CommonResponseDecoder(),
                new ControllerHolder(
                        new MainScreenController(),

                        new SignupController(),
                        new CreateUserController(userService)

//                                new LoginController(),
//
//                                new PersonalAreaController(),
//                                new FindOpponentController(),
//                                new FightController(),
//                                new FightResultController()
                )
        )) {
            server.run();
        }
    }
}
