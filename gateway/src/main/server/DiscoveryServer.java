package main.server;

import main.network.ServiceRegistry;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DiscoveryServer implements Runnable {

    private final ServerSocket serverSocket;
    public static final int port = 12345;

    public DiscoveryServer() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            Class.forName("main.network.ServiceRegistry");

            while (true) {
                Socket requester = serverSocket.accept();

                new Thread(() -> {
                    try {
                        DataInputStream in = new DataInputStream(requester.getInputStream());

                        int instancePort = in.readInt();
                        ServiceRegistry.register(instancePort);

                        in.close();
                        requester.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (Exception e) {
            System.out.println("Servidor encerrado.");
        }
    }
}
