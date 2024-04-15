package main.network;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ServiceRegistry {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Map<Integer, LocalDateTime> registry = new HashMap<>();
    private static final List<Integer> instances = new ArrayList<>();
    private static int loadBalancer = 0;

    static {
        scheduler.scheduleAtFixedRate(() -> {
            if (registry.values().stream().noneMatch(l -> l.isBefore(LocalDateTime.now().minusSeconds(5))))
                return;

            synchronized (ServiceRegistry.class) {
                registry.entrySet().removeIf(
                        l -> l.getValue().isBefore(LocalDateTime.now().minusSeconds(5))
                );

                instances.clear();
                instances.addAll(registry.keySet());

                if (registry.isEmpty())
                    loadBalancer = 0;
                else
                    loadBalancer %= registry.size();

                System.out.println("Servidor desconectado. Lista de servidores disponíveis: " + Arrays.toString(instances.toArray()));
            }
        }, 3, 3, TimeUnit.SECONDS);
    }

    public static void register(int port) {
        if (!registry.containsKey(port))
            synchronized (ServiceRegistry.class) {
                instances.add(port);
                registry.put(port, LocalDateTime.now());
                System.out.println("Novo servidor descoberto na porta " + port);
            }
        else
            registry.replace(port, LocalDateTime.now());
    }

    public static int getInstance() throws ServiceUnavailableException {
        if (registry.isEmpty())
            throw new ServiceUnavailableException("Serviço inalcançável.");

        synchronized(ServiceRegistry.class) {
            int port = instances.get(loadBalancer++);
            loadBalancer %= registry.size();
            return port;
        }
    }
}