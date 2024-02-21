package tcp.reactor;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Reactor {

    @Getter
    private final Selector demultiplexer;

    private boolean running = false;

    private final Map<Integer, EventHandler> registeredHandlers = new ConcurrentHashMap<>();

    public Reactor(Selector demultiplexer) throws IOException {
        this.demultiplexer = demultiplexer;
    }

    public void registerHandler(int eventType, EventHandler handler) {
        registeredHandlers.put(eventType, handler);
    }

    public void RemoveHandler(int eventType) {
        registeredHandlers.remove(eventType);
    }

    public void registerChannel(int eventType, SelectableChannel channel) throws ClosedChannelException {
        channel.register(demultiplexer, eventType);
        demultiplexer.wakeup();
    }

    public void dispatch() {
        running = true;
        while (running && demultiplexer.isOpen()) {
            try {
                demultiplexer.select();
                var selectedKeys = demultiplexer.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    var key = selectedKeys.next();
                    selectedKeys.remove();
                    var handler = registeredHandlers.get(key.interestOps());
                    if (handler != null) {
                        handler.handle(key);
                    }
                }
            } catch (IOException e) {
                log.error("Error occurred in Reactor: {}", e.getMessage());
            }
        }
        log.info("Reactor stopped");
    }

    public void stop() {
        running = false;

        try {
            this.demultiplexer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
