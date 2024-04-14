package network;

import network.annotations.Controller;
import network.annotations.Path;
import src.main.java.Request;
import src.main.java.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

            if (stream == null)
                throw new RuntimeException();

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
}
