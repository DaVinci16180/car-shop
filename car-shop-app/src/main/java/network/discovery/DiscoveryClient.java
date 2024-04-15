package network.discovery;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

public class DiscoveryClient implements Consumer<Integer> {
    @Override
    public void accept(Integer port) {
        try(Socket socket = new Socket(InetAddress.getLocalHost(), 12345)) {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(port);
            out.close();
        }catch (Exception ignored) {}
    }
}
