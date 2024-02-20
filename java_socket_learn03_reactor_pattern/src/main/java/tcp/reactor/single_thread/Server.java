package tcp.reactor.single_thread;

import tcp.reactor.Reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class Server {

    private final int port;
    private final Reactor reactor;
    public Server(int port) throws IOException {
        this.port = port;
        this.reactor = new Reactor();
    }

    public void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        reactor.registerHandler(SelectionKey.OP_ACCEPT, new AcceptHandler(reactor.getDemultiplexer()));
        reactor.registerHandler(SelectionKey.OP_READ, new ReadHandler(reactor.getDemultiplexer()));
        reactor.registerHandler(SelectionKey.OP_WRITE, new WriteHandler());
        reactor.registerChannel(SelectionKey.OP_ACCEPT, serverSocketChannel);

        new Thread(reactor::dispatch).start();
    }

    public void stop() {
        reactor.stop();
    }
}
