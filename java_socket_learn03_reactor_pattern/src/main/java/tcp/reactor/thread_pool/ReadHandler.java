package tcp.reactor.thread_pool;

import lombok.extern.slf4j.Slf4j;
import tcp.reactor.EventHandler;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ReadHandler implements EventHandler {

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);
    @Override
    public void handle(SelectionKey key) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        Selector demultiplexer = key.selector();
        try {
            StringBuilder content = new StringBuilder();
            buffer.clear();
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                content.append(new String(buffer.array()).trim());
                buffer.clear();
            }

            log.info("ReadHandler: {}", content);
            threadPool.execute(() -> {
                try {
                    businessLogic(content.toString(), socketChannel, demultiplexer);
                } catch (ClosedChannelException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            key.cancel();
            try {
                socketChannel.close();
            } catch (Exception ex) {
                log.error("Error occurred in ReadHandler: ", ex);
            }
        }
    }

    private void businessLogic(String message, SocketChannel socketChannel,Selector demultiplexer) throws ClosedChannelException {
        if("hello server".equals(message)) {
            write("hello client", socketChannel, demultiplexer);
        } else {
            write("unrecognized message", socketChannel, demultiplexer);
        }
    }

    private void write(String message, SocketChannel socketChannel, Selector demultiplexer) throws ClosedChannelException {
        socketChannel.register(demultiplexer, SelectionKey.OP_WRITE, message);
        demultiplexer.wakeup();
    }
}
