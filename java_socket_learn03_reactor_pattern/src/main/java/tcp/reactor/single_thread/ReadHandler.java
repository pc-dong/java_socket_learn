package tcp.reactor.single_thread;

import lombok.extern.slf4j.Slf4j;
import tcp.reactor.EventHandler;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

@Slf4j
public class ReadHandler implements EventHandler {
    
    private final Selector demultiplexer;
    
    public ReadHandler(Selector demultiplexer) {
        this.demultiplexer = demultiplexer;
    }
    
    @Override
    public void handle(SelectionKey key) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            StringBuilder content = new StringBuilder();
            buffer.clear();
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                content.append(new String(buffer.array()).trim());
                buffer.clear();
            }
            
            businessLogic(content.toString(), socketChannel);
        } catch (Exception e) {
            key.cancel();
            try {
                socketChannel.close();
            } catch (Exception ex) {
                log.error("Error occurred in ReadHandler: {}", ex.getMessage());
            }
        }
    }

    private void businessLogic(String message, SocketChannel socketChannel) throws ClosedChannelException {
        if("hello server".equals(message)) {
            write("hello client", socketChannel);
        } else {
            write("unrecognized message", socketChannel);
        }
    }

    private void write(String message, SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(demultiplexer, SelectionKey.OP_WRITE, message);
        demultiplexer.wakeup();
    }
}
