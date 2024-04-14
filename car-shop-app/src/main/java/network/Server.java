package network;

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

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket requester = serverSocket.accept();

                Thread thread = new Thread(() -> {
                    Firewall.checkConnection(requester);

                    try {
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
        } catch (IOException e) {
            System.out.println("Servidor encerrado.");
        }
    }
}