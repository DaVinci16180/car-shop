package network.discovery;

import network.Server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeregisterHeartbeat {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        scheduler.scheduleAtFixedRate(() -> {
            new DiscoveryClient().accept(Server.port);
        }, 3, 3, TimeUnit.SECONDS);
    }
}
