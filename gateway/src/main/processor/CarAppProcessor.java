package main.processor;

import main.network.Client;
import main.network.ServiceRegistry;
import src.main.java.Request;
import src.main.java.Response;

import javax.naming.ServiceUnavailableException;
import java.util.function.Function;

public class CarAppProcessor implements Function<Request, Response> {

    private static final Client client = Client.getInstance();

    @Override
    public Response apply(Request request) {
        try {
            int port = ServiceRegistry.getInstance();
            return client.execute(request, port);
        } catch (ServiceUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
}
