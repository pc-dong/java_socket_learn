package tcp.reactor.single_thread;

import lombok.extern.slf4j.Slf4j;
import tcp.reactor.EventHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@Slf4j
public class AcceptHandler implements EventHandler {

    private final Selector demultiplexer;

    public AcceptHandler(Selector demultiplexer) {
        this.demultiplexer = demultiplexer;
    }

    @Override
    public void handle(SelectionKey key) {
        try (ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel()) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(demultiplexer, SelectionKey.OP_READ);
        } catch (Exception e) {
            log.error("Error occurred in AcceptHandler: {}", e.getMessage());
        }
    }
}
