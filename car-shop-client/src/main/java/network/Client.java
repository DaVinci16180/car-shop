package network;

import src.main.java.CryptographyService;
import src.main.java.Package;
import src.main.java.Request;
import src.main.java.Response;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;

public class Client {

    private static final class InstanceHolder {
        private static final Client instance = new Client();
    }

    public static Client getInstance() {
        return Client.InstanceHolder.instance;
    }

    private final LocalStorage storage = LocalStorage.getInstance();

    private static final String serverAddress = "0.0.0.0";
    private static final int serverPort = 8080;
    private static final int rsaServerPort = 8081;

    private Client() {}

    public Response execute(Request request, boolean sign) {
        verifyServerKey();
        try(Socket socket = new Socket(serverAddress, serverPort)) {
            if (sign && storage.hmac == null) {
                storage.error = true;
                throw new SecurityException("Chave de acesso (Hmac) não configurada.");
            }

            request.addHeader("rsa-public-key", storage.keyPair.getPublic());
            request.addHeader("token", storage.token);

            if (sign)
                request.addHeader("sign", CryptographyService.sign(request, storage.hmac));

            SecretKey key = CryptographyService.generateKey("AES");

            byte[] encryptedData = CryptographyService.encryptAES(request, key);
            byte[] encryptedKey = CryptographyService.encryptRSA(key, storage.serverPublicKey);

            Package send = new Package(encryptedKey, encryptedData);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(send);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Package recv = (Package) in.readObject();

            key = (SecretKey) CryptographyService.decryptRSA(recv.key(), storage.keyPair.getPrivate());
            Response response = (Response) CryptographyService.decryptAES(recv.data(), key);

            out.close();
            in.close();

            boolean success = (Boolean) response.getBody().get("success");
            if (!success) {
                storage.error = true;
                storage.errorMessage = (String) response.getBody().get("message");

                throw new Error();
            }

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Response execute(Request request, int port) {
        try(Socket socket = new Socket(serverAddress, port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Response response = (Response) in.readObject();

            in.close();
            out.close();

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyServerKey() {
        if (storage.serverPublicKey == null) {
            try (Socket socket = new Socket(serverAddress, rsaServerPort);
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
                storage.serverPublicKey = (PublicKey) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
