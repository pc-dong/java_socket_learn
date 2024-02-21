package tcp.reactor.multi_reactor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tcp.reactor.single_thread.Client;
import tcp.reactor.single_thread.Server;

class ServerTest {
    @Test
    void start() {
        int port = 6666;
        tcp.reactor.single_thread.Server server = new Server(port);
        server.start();


        tcp.reactor.single_thread.Client client = new Client();
        String result = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result);

        String result1 = client.sendMessage("hello server", "localhost", port);
        Assertions.assertEquals("hello client", result1);

        server.stop();
    }
}