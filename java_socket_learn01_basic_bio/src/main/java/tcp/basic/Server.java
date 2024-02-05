package tcp.basic;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {
    private final int port;
    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("server started");
            try (Socket clientSocket = serverSocket.accept()) {
                log.info("client connected");
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    String greeting = in.readLine();
                    if ("hello server".equals(greeting)) {
                        out.println("hello client");
                    } else {
                        out.println("unrecognised greeting");
                    }
                }
            }
        } catch (java.io.IOException e) {
            log.error("error", e);
        }
    }
}
