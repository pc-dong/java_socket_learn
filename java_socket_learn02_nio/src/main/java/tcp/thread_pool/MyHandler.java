package tcp.thread_pool;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class MyHandler implements Runnable {

    private Selector selector;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public MyHandler() throws IOException {
        selector = Selector.open();
    }

    public void register(SocketChannel socketChannel) throws IOException {
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isReadable()) {
                        SocketChannel clientSocket = (SocketChannel) key.channel();
                        buffer.clear();
                        clientSocket.read(buffer);
                        buffer.flip();
                        String greeting = new String(buffer.array()).trim();
                        log.info("received: {}", greeting);
                        threadPool.execute(() -> {
                            try {
                                businessLogic(greeting, clientSocket, selector);
                            } catch (ClosedChannelException e) {
                                log.error("error", e);
                            }
                        });
                    }

                    if (key.isWritable()) {
                        SocketChannel clientSocket = (SocketChannel) key.channel();
                        String message = (String) key.attachment();
                        clientSocket.write(ByteBuffer.wrap(message.getBytes()));
                        clientSocket.close();
                    }
                }
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    private static void businessLogic(String greeting, SocketChannel clientSocket, Selector selector) throws
            ClosedChannelException {
        if ("hello server".equals(greeting)) {
            clientSocket.register(selector, SelectionKey.OP_WRITE, "hello client");
        } else {
            clientSocket.register(selector, SelectionKey.OP_WRITE, "unrecognised greeting");
        }
        selector.wakeup();
    }
}
