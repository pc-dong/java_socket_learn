package tcp.reactor.single_thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ServerTest {
    @Test
    void start() {
        int port = 6666;
        Server server = new Server(port);
        server.start();


        Client client = new Client();
        String result = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result);

        String result1 = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result1);

        server.stop();
    }
}