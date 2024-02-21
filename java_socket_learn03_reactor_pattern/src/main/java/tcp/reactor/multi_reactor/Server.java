package tcp.reactor.multi_reactor;

import lombok.extern.slf4j.Slf4j;
import tcp.reactor.Reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

@Slf4j
public class Server {

    private final int port;
    private Reactor reactor;
    private ServerSocketChannel serverSocketChannel;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        new Thread(() -> {
            try {
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind(new InetSocketAddress("localhost", port));
                serverSocketChannel.configureBlocking(false);
                reactor = new Reactor();
                reactor.registerHandler(SelectionKey.OP_ACCEPT, new AcceptHandler());

                reactor.registerChannel(SelectionKey.OP_ACCEPT, serverSocketChannel);
                reactor.dispatch();
            } catch (IOException e) {
                log.error("error", e);
            }

        }).start();
    }

    public void stop() {
        reactor.stop();

        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }
}
