package main.network;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Firewall {

    private static final Map<InetAddress, List<Long>> requestLog = new HashMap<>();
    private static final InetAddress clientAddress;
    static {
        try {
            clientAddress = InetAddress.getByName("192.168.0.6");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static void monitor(Socket requester) throws AccessDeniedException {
        try {
            checkFrequency(requester);
            checkOrigin(requester);
        } catch (AccessDeniedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void checkFrequency(Socket requester) throws AccessDeniedException {
        synchronized (requester.getInetAddress()) {
            InetAddress address = requester.getInetAddress();
            if (!requestLog.containsKey(address))
                requestLog.put(address, new ArrayList<>());

            List<Long> times = requestLog.get(address);

            long oneMinuteAgo = LocalDateTime.now()
                    .minusMinutes(1)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            if (times.stream().filter(t -> t.compareTo(oneMinuteAgo) > 0).count() >= 15)
                throw new AccessDeniedException("Limite de requisições excedido.");

            long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            requestLog.get(address).add(now);
        }
    }

    private static void checkOrigin(Socket requester) throws AccessDeniedException {
        if (!requester.getInetAddress().getHostAddress().equals(clientAddress.getHostAddress()))
            throw new AccessDeniedException("Bloqueado pela politica de CORS.");
    }
}
