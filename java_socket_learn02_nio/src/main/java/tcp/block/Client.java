package tcp.block;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public String sendMessage(String message, String host, int port) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try (SocketChannel client = SocketChannel.open(new InetSocketAddress(host, port))) {
            client.write(ByteBuffer.wrap(message.getBytes()));
            client.read(buffer);
            buffer.flip();
            return new String(buffer.array()).trim();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
