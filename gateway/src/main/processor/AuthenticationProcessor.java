package main.processor;

import src.main.java.IAuthentication;
import src.main.java.Request;
import src.main.java.Response;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.function.Function;

public class AuthenticationProcessor implements Function<Request, Response> {

    private final IAuthentication auth;

    public AuthenticationProcessor() {
        try {
            auth = (IAuthentication) LocateRegistry
                    .getRegistry(Registry.REGISTRY_PORT)
                    .lookup("auth");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response apply(Request request) {
        return switch (request.getPath()) {
            case "user/login"         -> signIn(request);
            case "user/register"      -> signUp(request);
            case "user/configureHmac" -> hmac(request);
            case "user/logout"        -> signOut(request);
            default                   -> Response.fail();
        };
    }

    private Response hmac(Request request) {
        String username = (String) request.getBody().get("username");
        String password = (String) request.getBody().get("password");

        try {
            String token = auth.signIn(username, password);
            SecretKey hmac = auth.getHmac(token);
            Map<String, String> data = auth.getUserData(token);

            Response response = Response.success();
            response.addBody("hmac", hmac);
            response.addBody("token", token);
            response.addBody("user-name", data.get("name"));
            response.addBody("user-role", data.get("role"));

            return response;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (AccessDeniedException e) {
            Response response = Response.fail();
            response.addBody("message", e.getMessage());
            return response;
        }
    }

    private Response signIn(Request request) {
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
        } catch (AccessDeniedException e) {
            Response response = Response.fail();
            response.addBody("message", e.getMessage());
            return response;
        }

        Response response = Response.success();
        response.addBody("token", token);
        response.addBody("user-name", name);
        response.addBody("user-role", role);

        return response;
    }

    private Response signUp(Request request) {
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

    private Response signOut(Request request) {
        String token = String.valueOf(request.getBody().get("token"));

        try {
            auth.signOut(token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        return Response.success();
    }

}
