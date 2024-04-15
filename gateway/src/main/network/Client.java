package main.network;

import src.main.java.Request;
import src.main.java.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static final class InstanceHolder {
        private static final Client instance = new Client();
    }

    public static Client getInstance() {
        return Client.InstanceHolder.instance;
    }

    private Client() {}

    public Response execute(Request request, int targetPort) {
        try(Socket socket = new Socket(InetAddress.getLocalHost(), targetPort)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Response response = (Response) in.readObject();

            out.close();
            in.close();

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
