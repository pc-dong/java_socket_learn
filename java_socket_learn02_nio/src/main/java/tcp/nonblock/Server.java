package tcp.nonblock;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
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
            serverSocket.configureBlocking(false); // blocking mode
            log.info("server started");
            SocketChannel clientSocket = null;
            do {
                clientSocket = serverSocket.accept();
            } while (clientSocket == null);


            clientSocket.configureBlocking(false);

            buffer.clear();
            while (clientSocket.read(buffer) == 0) {
                // wait for data
            }
            buffer.flip();
            String greeting = new String(buffer.array()).trim();
            log.info("received: {}", greeting);

            log.info("client connected");
            if ("hello server".equals(greeting)) {
                clientSocket.write(ByteBuffer.wrap("hello client".getBytes()));
            } else {
                clientSocket.write(ByteBuffer.wrap("unrecognised greeting".getBytes()));
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
