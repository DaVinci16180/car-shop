package controller;

import network.annotations.Controller;
import network.annotations.Path;
import src.main.java.IAuthentication;
import src.main.java.Request;
import src.main.java.Response;

import javax.crypto.SecretKey;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

@Controller(path = "user")
public class UserController {

    private static class InstanceHolder {
        private static final UserController instance = new UserController();
    }

    public static UserController getInstance() {
        return UserController.InstanceHolder.instance;
    }

    IAuthentication auth;

    private UserController() {
        try {
            auth = (IAuthentication) LocateRegistry
                    .getRegistry(Registry.REGISTRY_PORT)
                    .lookup("auth");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Path(value = "login", _public = true)
    public Object login(Request request) {
        String username = String.valueOf(request.getBody().get("username"));
        String password = String.valueOf(request.getBody().get("password"));

        String token, name, role;
        try {
            token = auth.signIn(username, password);

            Map<String, String> data = auth.getUserData(token);
            name = data.get("name");
            role = data.get("role");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Response response = Response.success();
        response.addBody("token", token);
        response.addBody("user-name", name);
        response.addBody("user-role", role);

        return response;
    }

    @Path(value = "register", _public = true)
    public Object register(Request request) {
        String name = String.valueOf(request.getBody().get("name"));
        String username = String.valueOf(request.getBody().get("username"));
        String password = String.valueOf(request.getBody().get("password"));

        String token;
        SecretKey hmac;
        try {
            token = auth.signUp(username, password, name);
            hmac = auth.getHmac(token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Response response = Response.success();
        response.addBody("hmac", hmac);
        response.addBody("token", token);
        response.addBody("user-name", name);
        response.addBody("user-role", "CUSTOMER");

        return response;
    }

    @Path(value = "logout")
    public Object logout(Request request) {
        String token = String.valueOf(request.getBody().get("token"));

        try {
            auth.signOut(token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return Response.success();
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
    @Path(value = "configureHmac", _public = true)
    public Object configureHmac(Request request) {
        String username = (String) request.getBody().get("username");
        String password = (String) request.getBody().get("password");

        try {
            String token = auth.signIn(username, password);
            SecretKey hmac = auth.getHmac(token);

            Response response = Response.success();
            response.addBody("hmac", hmac);

            return response;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
