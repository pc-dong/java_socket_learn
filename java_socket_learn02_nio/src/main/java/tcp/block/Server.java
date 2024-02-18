package tcp.block;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@Slf4j
public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            serverSocket.bind(new InetSocketAddress(port));
            serverSocket.configureBlocking(true); // blocking mode
            log.info("server started");
            try (SocketChannel clientSocket = serverSocket.accept()) {
                log.info("client connected");
                clientSocket.configureBlocking(true);

                buffer.clear();
                clientSocket.read(buffer);
                buffer.flip();
                String greeting = new String(buffer.array()).trim();
                log.info("received: {}", greeting);


                if ("hello server".equals(greeting)) {
                    clientSocket.write(ByteBuffer.wrap("hello client".getBytes()));
                } else {
                    clientSocket.write(ByteBuffer.wrap("unrecognised greeting".getBytes()));
                }
            }
        } catch (IOException e) {
            log.error("error", e);
        }
    }
}
