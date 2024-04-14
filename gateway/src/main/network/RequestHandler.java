package main.network;

import src.main.java.IAuthentication;
import src.main.java.Request;
import src.main.java.Response;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RequestHandler {

    private final IAuthentication auth;

    private RequestHandler() {
        try {
            auth = (IAuthentication) LocateRegistry
                    .getRegistry(Registry.REGISTRY_PORT)
                    .lookup("auth");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final class InstanceHolder {
        private static final RequestHandler instance = new RequestHandler();
    }

    public static RequestHandler getInstance() {
        return InstanceHolder.instance;
    }

    public Response handle(Request request) {
        // passa pelo firewall

        URL.Route route = URL.routes.get(request.getPath());
        if (!checkRequest(request, route))
            return Response.fail();

        try {
            return route
                    .processor()
                    .getDeclaredConstructor()
                    .newInstance()
                    .apply(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkRequest(Request request, URL.Route route) {
        try {
            boolean valid = validateSession(request, route);
            boolean whole = checkIntegrity(request, route);
            boolean grant = checkPermission(request, route);

            return valid && whole && grant;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkPermission(Request request, URL.Route route) throws RemoteException {
        if (!route.needPermission())
            return true;

        String token = (String) request.getHeaders().get("token");
        if (token == null)
            return false;

        return auth.hasRole(token, new String[] { route.permission() });
    }

    private boolean validateSession(Request request, URL.Route route) throws RemoteException, NotBoundException {
        if (route.is_public())
            return true;

        String token = (String) request.getHeaders().get("token");

        if (token == null)
            return false;

        return auth.validate(token);
    }

    private boolean checkIntegrity(Request request, URL.Route route) {
        if (!route.is_signed())
            return true;

        String token = (String) request.getHeaders().get("token");

        try {
            String sign = (String) request.getHeaders().get("sign");
            request.removeHeader("sign");

            return auth.checkSign(sign, request, token);
        } catch (Exception e) {
            return false;
        }
    }
}
