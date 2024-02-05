package tcp.thread_pool;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server {
    private final int port;
    private boolean running = false;

    private final static ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        running = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("server started");
            while (running) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> new ClientHandler(clientSocket).handle());
            }
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    public void stop() {
        this.running = false;
    }
}
