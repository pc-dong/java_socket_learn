package tcp.reactor.single_thread;

import lombok.extern.slf4j.Slf4j;
import tcp.reactor.EventHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import static java.nio.ByteBuffer.*;

@Slf4j
public class WriteHandler implements EventHandler {
    @Override
    public void handle(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        String message = (String) key.attachment();
        try {
            socketChannel.write(wrap(message.getBytes()));
            socketChannel.close();
        } catch (Exception e) {
            log.error("Error occurred in WriteHandler: {}", e.getMessage());
        }
    }
}
