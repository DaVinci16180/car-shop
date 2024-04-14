package main.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class DiscoveryServer implements Runnable {

    private final ServerSocket serverSocket;
    public static final int port = 8082;

    public DiscoveryServer() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket requester = serverSocket.accept();

                new Thread(() -> {
                    try {
                        ObjectInputStream in = new ObjectInputStream(requester.getInputStream());

                        String clientAddress = (String) in.readObject();
                        String clientHost = clientAddress.split(":")[0];
                        int clientPort = Integer.parseInt(clientAddress.split(":")[1]);

                        try(Socket socket = new Socket(clientHost, clientPort)) {
                            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                            String sb =
                                    InetAddress.getLocalHost().getHostAddress() + ";" +
                                    Server.port + ";" +
                                    RSAServer.port;

                            out.writeObject(sb);
                            out.close();
                        }

                        in.close();
                        requester.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Servidor encerrado.");
        }
    }
}
