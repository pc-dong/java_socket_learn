package tcp.thread_pool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServerTest {
    @Test
    void start() {
        int port = 5457;
        Server server = new Server(port);
        new Thread(server::start).start();

        Client client = new Client();
        String result = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result);

        String result2 = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result2);

        server.stop();
    }
}