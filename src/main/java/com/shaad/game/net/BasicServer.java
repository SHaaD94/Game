package com.shaad.game.net;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class BasicServer implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(BasicServer.class);

    private ServerSocket serverSocket;


    public BasicServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            log.error("Shit happened", e);
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            for (; ; ) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    Request request = parseRequest(clientSocket);

                    System.out.println(request);

                    out.println("hello client");
                    out.close();
                }
            }
        } catch (Exception e) {
            log.error("Shit happened", e);
            throw new RuntimeException(e);
        }
    }

    private Request parseRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //parse first line containing:
        //METHOD *PATH* HTTP
        String firstLine = in.readLine();
        String[] split = firstLine.split(" ");
        String method = split[0];
        String path = split[1];

        Map<String, String> params = new HashMap<>();
        String[] splittedPath = path.split("\\?");
        path = splittedPath[0];
        if (splittedPath.length != 1) {
            String paramString = splittedPath[1];
            Arrays.stream(paramString.split("&"))
                    .map(x -> x.split("="))
                    .filter(x -> x.length == 2)
                    .forEach(splitterParam -> params.put(splitterParam[0], splitterParam[1]));
        }

        //parse headers
        List<String> headers = new ArrayList<>();
        int contentLength = 0;
        String header = in.readLine();
        while (!header.isEmpty()) {
            headers.add(header);
            //if request contains body, header content length will appear
            if (header.contains("Content-Length")) {
                contentLength = Integer.parseInt(header.split(": ")[1]);
            }
            header = in.readLine();
        }
        String body = null;
        if (contentLength != 0) {
            //read exact count of elements from body
            char[] buffer = new char[contentLength];
            in.read(buffer);
            body = String.copyValueOf(buffer);
        }

        return new Request(method, path, headers, params, body);
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            log.error("Shit happened", e);
            throw new RuntimeException(e);
        }
    }
}
