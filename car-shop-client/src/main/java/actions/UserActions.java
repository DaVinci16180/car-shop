package actions;

import network.Client;
import network.LocalStorage;
import src.main.java.Request;
import src.main.java.Response;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserActions {
    private static final LocalStorage storage = LocalStorage.getInstance();
    private static final Client client = Client.getInstance();

    public static void login(String username, String password) {
        Request request = new Request();
        request.setPath("user/login");

        request.addBody("username", username);
        request.addBody("password", password);

        Response response = client.execute(request, true);
        storage.token = (String) response.getBody().get("token");
        storage.userName = (String) response.getBody().get("user-name");
        storage.userRole = (String) response.getBody().get("user-role");
    }

    public static void register(
            String name,
            String username,
            String password
    ) {
        Request request = new Request();
        request.setPath("user/register");

        request.addBody("name", name);
        request.addBody("username", username);
        request.addBody("password", password);

        Response response = client.execute(request, false);
        storage.hmac = (SecretKey) response.getBody().get("hmac");
        storage.token = (String) response.getBody().get("token");
        storage.userName = (String) response.getBody().get("user-name");
        storage.userRole = (String) response.getBody().get("user-role");
    }

    /**
     * Da forma que está implementado, o usuário só tem
     * acesso à chave Hmac no ato do cadastro de uma nova
     * conta. Além disso, o login exige assinatura. Devido
     * a isso, as contas criadas no setup inicial não
     * possuirão uma chave e não poderão ser acessadas.
     * Este método, usado apenas para depuração, cria e
     * retorna uma chave Hmac para essas contas.
     */
    public static void configureHmac(String accountNumber, String password) {
        Request request = new Request();
        request.setPath("user/configureHmac");

        request.addBody("username", accountNumber);
        request.addBody("password", password);

        Response response = client.execute(request, false);
        storage.hmac = (SecretKey) response.getBody().get("hmac");
    }

    public static void logout() {
        Request request = new Request();
        request.setPath("user/logout");

        request.addBody("token", storage.token);

        client.execute(request, true);
        storage.clearSession();
    }

    public static void purchaseCar(int id) {
        Request request = new Request();
        request.setPath("car-customer/buy");
        request.addBody("id", id);

        Response response = client.execute(request, false);

        return ;
    }
}
