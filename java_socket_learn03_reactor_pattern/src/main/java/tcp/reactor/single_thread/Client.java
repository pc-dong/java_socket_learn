package tcp.reactor.single_thread;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class Client {
    public String sendMessage(String message, String host, int port) {
        try (SocketChannel client = SocketChannel.open(new InetSocketAddress(host, port))){
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.configureBlocking(true);
            client.write(ByteBuffer.wrap(message.getBytes()));
            client.read(buffer);
            buffer.flip();

            return new String(buffer.array()).trim();
        } catch (java.io.IOException e) {
            log.error("error", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        String response = client.sendMessage("hello server", "localhost", 8080);
        log.info("response: {}", response);
    }
}
