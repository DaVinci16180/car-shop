package network;

import src.main.java.CryptographyService;
import repository.CarRepository;
import network.annotations.Controller;
import network.annotations.Path;
import src.main.java.IAuthentication;
import src.main.java.Request;
import src.main.java.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.stream.Collectors;

public class RequestHandler {

    private static final class InstanceHolder {
        private static final RequestHandler instance = new RequestHandler();
    }

    public static RequestHandler getInstance() {

        return RequestHandler.InstanceHolder.instance;
    }

    private RequestHandler() {
        findControllers();
    }

    private final Map<String, Class<?>> controllers = new HashMap<>();
    private final Set<String> bypassHmac = new HashSet<>(); {
        bypassHmac.add("auth/register");
        bypassHmac.add("auth/configureHmac"); // Apenas para depuração
    }

    private final CarRepository carRepository = CarRepository.getInstance();

    public Response handle(Request request) {
        String[] path = request.getPath().split("/");
        Class<?> clazz = controllers.get(path[0]);
        Response response = new Response();

        try {
            Method method = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(Path.class))
                    .filter(m -> m.getAnnotation(Path.class).value().equals(path[1]))
                    .findFirst()
                    .orElseThrow();

            boolean valid = validateSession(method, request);
//            boolean whole = checkIntegrity(request);

            if (!(valid)) {
                response.addBody("success", false);
                response.addBody("message", "Acesso negado!");
                return response;
            }

            Object instance = clazz.getMethod("getInstance").invoke(null);

            try {
                response = (Response) method.invoke(instance, request);
            } catch (Exception e) {
                response.addBody("success", false);
                response.addBody("message", e.getMessage());
            }

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("# " + request);
            System.out.println("# " + response);
        }
    }

    private void findControllers() {
        try {
            String packageName = "controller";
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            Set<Class<?>> classes = reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(line -> {
                        try {
                            line = line.split("\\.")[0];
                            return Class.forName(packageName.replaceAll("/", ".") + "." + line);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());

            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(Controller.class)) {
                    Controller annotation = clazz.getAnnotation(Controller.class);
                    this.controllers.put(annotation.path(), clazz);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateSession(Method method, Request request) throws RemoteException, NotBoundException {
        if (!method.getAnnotation(Path.class)._public()) {
            if (request.getHeaders().get("token") == null)
                return false;

            IAuthentication auth = (IAuthentication) LocateRegistry
                    .getRegistry(Registry.REGISTRY_PORT)
                    .lookup("auth");

            return auth.validate((String) request.getHeaders().get("token"));
        }

        return true;
    }

//    private boolean checkIntegrity(Request request) {
//        if (bypassHmac.contains(request.getPath()))
//            return true;
//
//        String number = (String) request.getBody().get("account-number");
//        Account account = accountService.findAccount(number);
//
//        try {
//            String sign = (String) request.getHeaders().get("sign");
//            request.removeHeader("sign");
//            String calc = CryptographyService.sign(request, account.getHmacKey());
//
//            return sign.equals(calc);
//        } catch (Exception e) {
//            return false;
//        }
//    }
}