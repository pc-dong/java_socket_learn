package tcp.reactor.multi_reactor;

import lombok.extern.slf4j.Slf4j;
import tcp.reactor.EventHandler;
import tcp.reactor.Reactor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.channels.SelectionKey.OP_READ;

@Slf4j
public class AcceptHandler implements EventHandler {


    private final List<Reactor> reactors;
    private int index;

    public AcceptHandler() throws IOException {
        this(4);
    }

    public AcceptHandler(int subReactorCount) throws IOException {
        reactors = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(subReactorCount);
        for(int i = 0; i < subReactorCount; i++) {
            Reactor reactor = new Reactor();
            reactor.registerHandler(OP_READ, new ReadHandler());
            reactor.registerHandler(SelectionKey.OP_WRITE, new WriteHandler());
            reactors.add(reactor);
            threadPool.execute(reactor::dispatch);
        }
    }

    @Override
    public void handle(SelectionKey key) {
        log.info("AcceptHandler: accept a connection");
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            reactors.get(index++ % reactors.size()).registerChannel(OP_READ, socketChannel);
        } catch (Exception e) {
            log.error("Error occurred in AcceptHandler: ", e);
        }
    }
}
