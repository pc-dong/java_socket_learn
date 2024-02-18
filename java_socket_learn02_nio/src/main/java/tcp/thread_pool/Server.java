package tcp.thread_pool;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

@Slf4j
public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            MyHandler handler = new MyHandler();
            new Thread(() -> {
                try {
                    try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
                        serverSocket.bind(new InetSocketAddress(port));
                        Selector selector = Selector.open();
                        serverSocket.configureBlocking(false);
                        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
                        while (true) {
                            selector.select();
                            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                            while (iterator.hasNext()) {
                                SelectionKey key = iterator.next();
                                iterator.remove();
                                if (key.isAcceptable()) {
                                    SocketChannel clientSocket = serverSocket.accept();
                                    handler.register(clientSocket);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("error", e);
                }
            }).start();

            new Thread(handler).start();
        } catch (Exception e) {
            log.error("error", e);
        }
    }


}
