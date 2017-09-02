package com.shaad.game;

import com.shaad.game.net.BasicServer;

public class App {
    public static void main(String[] args) {
        try (BasicServer basicServer = new BasicServer(8080)) {
            basicServer.run();
        }
    }
}
