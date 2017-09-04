package com.shaad.game.net;


import com.shaad.game.net.controller.ControllerHolder;
import com.shaad.game.net.decoder.CommonResponseDecoder;
import com.shaad.game.net.decoder.ResponseDecoder;
import com.shaad.game.net.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server implements AutoCloseable {
    private static final ExecutorService executors =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8);

    private final ResponseDecoder responseDecoder = new CommonResponseDecoder();
    private final ControllerHolder controllerHolder;

    private ServerSocket serverSocket;

    public Server(int port, ControllerHolder controllerHolder) {
        try {
            this.controllerHolder = controllerHolder;
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            log.error("Shit happened", e);
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            for (; ; ) {
                Socket clientSocket = serverSocket.accept();
                executors.submit(() -> {
                    try {
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        Request request = parseRequest(clientSocket);

                        System.out.println(request);

                        Response response = controllerHolder.handleRequest(request);

                        String decode = responseDecoder.decode(response);
                        out.println(decode);
                        out.close();
                    } catch (Exception e) {
                        log.error("Shit happened", e);
                        throw new RuntimeException(e);
                    }
                });
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

        return new Request(HttpMethod.valueOf(method), path, headers, params, body);
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
