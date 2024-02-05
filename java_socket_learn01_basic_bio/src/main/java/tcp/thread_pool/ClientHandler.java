package tcp.thread_pool;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class ClientHandler {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void handle() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String greeting = in.readLine();

            String response = handleBusiness(greeting);

            out.println(response);
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    private static String handleBusiness(String greeting) {
        if ("hello server".equals(greeting)) {
            return "hello client";
        } else {
            return  "unrecognised greeting";
        }
    }
}
