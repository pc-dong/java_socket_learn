package tcp.blocking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tcp.block.Client;
import tcp.block.Server;

class ServerTest {
    @Test
    void start() {
        int port = 6666;
        Server server = new Server(port);
        new Thread(server::start).start();

        Client client = new Client();
        String result = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result);
    }
}