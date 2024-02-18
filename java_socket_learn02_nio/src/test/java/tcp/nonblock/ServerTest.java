package tcp.nonblock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tcp.block.Client;
import tcp.block.Server;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    @Test
    void start() {
        int port = 6666;
        tcp.block.Server server = new Server(port);
        new Thread(server::start).start();

        tcp.block.Client client = new Client();
        String result = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result);
    }
}