package network;

import network.discovery.DiscoveryClient;
import src.main.java.Request;
import src.main.java.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    private final ServerSocket serverSocket;
    private final RequestHandler handler = RequestHandler.getInstance();
    public static Integer port;

    public Server() throws IOException {
        serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        try {
            new DiscoveryClient().accept(port);
            Class.forName("network.discovery.DeregisterHeartbeat");

            while (true) {
                Socket requester = serverSocket.accept();

                Thread thread = new Thread(() -> {
                    try {
                        Firewall.monitor(requester);

                        ObjectInputStream in = new ObjectInputStream(requester.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(requester.getOutputStream());

                        Request request = (Request) in.readObject();
                        Response response = handler.handle(request);

                        out.writeObject(response);
                        out.flush();

                        in.close();
                        out.close();
                        requester.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                thread.start();
            }
        } catch (Exception e) {
            System.out.println("Servidor encerrado.");
        }
    }
}