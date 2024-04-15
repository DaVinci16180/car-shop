package main.server;

import main.network.Firewall;
import main.network.RequestHandler;
import src.main.java.CryptographyService;
import src.main.java.Package;
import src.main.java.Request;
import src.main.java.Response;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.AccessDeniedException;
import java.security.PublicKey;

public class Server implements Runnable{

    private final ServerSocket serverSocket;
    private final RequestHandler handler = RequestHandler.getInstance();
    public static final int port = 8080;

    public Server() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket requester = serverSocket.accept();

                Thread thread = new Thread(() -> {
                    try {
                        ObjectInputStream in = new ObjectInputStream(requester.getInputStream());
                        ObjectOutputStream out = new ObjectOutputStream(requester.getOutputStream());

                        Request request = getRequest(in);
                        PublicKey userKey = (PublicKey) request.getHeaders().get("rsa-public-key");

                        Response response;
                        try {
                            Firewall.monitor(requester);
                            response = handler.handle(request);
                        } catch (AccessDeniedException e) {
                            response = Response.fail();
                            response.addBody("message", e.getMessage());
                        }

                        Package pack = generatePackage(response, userKey);

                        out.writeObject(pack);

                        try {
                            in.close();
                            out.close();
                            requester.close();
                        } catch (Exception ignored) {}
                    } catch (Exception e) {
                        try {
                            requester.close();
                        } catch (IOException ignored) {}
                        throw new RuntimeException(e);
                    }
                });

                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Servidor encerrado.");
        }
    }

    public Package generatePackage(Response response, PublicKey userKey) {
        SecretKey simetricKey = CryptographyService.generateKey("AES");
        byte[] data = CryptographyService.encryptAES(response, simetricKey);
        byte[] encryptedKey = CryptographyService.encryptRSA(simetricKey, userKey);
        return new Package(encryptedKey, data);
    }

    private Request getRequest(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Package pack = (Package) in.readObject();
        SecretKey simetricKey = (SecretKey) CryptographyService.decryptRSA(pack.key(), RSAServer.getPrivateKey());
        return (Request) CryptographyService.decryptAES(pack.data(), simetricKey);
    }
}