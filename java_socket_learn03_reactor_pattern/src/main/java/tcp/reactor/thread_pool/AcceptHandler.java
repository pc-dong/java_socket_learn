package tcp.reactor.thread_pool;

import lombok.extern.slf4j.Slf4j;
import tcp.reactor.EventHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static java.nio.channels.SelectionKey.OP_READ;

@Slf4j
public class AcceptHandler implements EventHandler {

    @Override
    public void handle(SelectionKey key) {
        log.info("AcceptHandler: accept a connection");
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(key.selector(), OP_READ);
        } catch (Exception e) {
            log.error("Error occurred in AcceptHandler: ", e);
        }
    }
}
