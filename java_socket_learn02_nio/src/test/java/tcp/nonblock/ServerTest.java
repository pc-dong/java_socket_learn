package tcp.nonblock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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