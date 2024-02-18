package tcp.multiplex;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try (ServerSocketChannel serverSocket = ServerSocketChannel.open();
             Selector selector = Selector.open();
        ) {
            serverSocket.bind(new InetSocketAddress(port));
            serverSocket.configureBlocking(false);

            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator =  selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        SocketChannel clientSocket = serverSocket.accept();
                        clientSocket.configureBlocking(false);
                        clientSocket.register(selector, SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        SocketChannel clientSocket = (SocketChannel) key.channel();
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
                }
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }
}
