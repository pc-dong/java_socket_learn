package udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Slf4j
public class Receiver {
    private final int port;
    private boolean running = false;

    public Receiver(int port) {
        this.port = port;
    }

    public void start() {
        running = true;
        try (DatagramSocket datagramSocket = new DatagramSocket(port)) {
            while (running) {
                byte[] buffer = new byte[1024];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(packet);
                log.info("received: {}", new String(buffer, 0, packet.getLength()));
            }
        } catch (IOException e) {
            log.error("error", e);
        }
    }

    public void stop() {
        this.running = false;
    }

    public static void main(String[] args) {
        Receiver server = new Receiver(5454);
        server.start();
    }
}
