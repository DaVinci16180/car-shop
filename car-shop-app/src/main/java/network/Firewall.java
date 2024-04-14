package network;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Firewall {
    public static void checkConnection(Socket requester) {
        try {
            String addr = InetAddress.getLocalHost().getHostAddress();
            if (!requester.getInetAddress().getHostAddress().equals(addr))
                throw new SecurityException("Bloqueado pelo firewall");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
