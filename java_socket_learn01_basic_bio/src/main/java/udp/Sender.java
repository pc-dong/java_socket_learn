package udp;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sender {
    public static void main(String[] args) {
        try (java.net.DatagramSocket socket = new java.net.DatagramSocket()) {
            byte[] buffer = "hello server".getBytes();
            java.net.DatagramPacket packet = new java.net.DatagramPacket(buffer, buffer.length, java.net.InetAddress.getByName("localhost"), 5454);
            socket.send(packet);
        } catch (java.io.IOException e) {
            log.error("error", e);
        }
    }
}
