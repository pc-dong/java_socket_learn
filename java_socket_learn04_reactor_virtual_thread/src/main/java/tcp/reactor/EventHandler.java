package tcp.reactor;

import java.nio.channels.SelectionKey;

public interface EventHandler {
    void handle(SelectionKey key);
}
