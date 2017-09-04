package com.shaad.game;

import com.google.common.collect.Lists;
import com.shaad.game.net.Server;
import com.shaad.game.net.controller.ControllerHolder;
import com.shaad.game.net.controller.HelloWorldController;

public class App {
    public static void main(String[] args) {
        try (Server server = new Server(8080,
                new ControllerHolder(
                        Lists.newArrayList(
                                new HelloWorldController()
                        )
                )
        )) {
            server.run();
        }
    }
}
