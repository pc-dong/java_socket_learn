package tcp.single_thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public String sendMessage(String message, String host, int port) {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(message);
            return in.readLine();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
