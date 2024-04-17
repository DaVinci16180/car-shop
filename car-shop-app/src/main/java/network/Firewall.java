package network;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.AccessDeniedException;

public class Firewall {
    public static void monitor(Socket requester) throws AccessDeniedException {
        try {
            String localhost = InetAddress.getLocalHost().getHostAddress();

            if (requester.getInetAddress().getHostAddress().equals(localhost))
                throw new AccessDeniedException("Bloqueado pela politica de CORS.");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
