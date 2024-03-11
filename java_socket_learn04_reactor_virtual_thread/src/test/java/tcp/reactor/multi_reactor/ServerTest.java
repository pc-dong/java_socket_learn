package tcp.reactor.multi_reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServerTest {
    @Test
    void start() throws InterruptedException {
        int port = 6666;
        Server server = new Server(port);
        server.start();

        Thread.sleep(100);

        Client client = new Client();
        String result = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result);

        String result1 = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result1);

        server.stop();
    }
}