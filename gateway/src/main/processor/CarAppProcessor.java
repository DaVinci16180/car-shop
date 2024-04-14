package main.processor;

import main.network.Client;
import src.main.java.Request;
import src.main.java.Response;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CarAppProcessor implements Function<Request, Response> {

    private static final Client client = Client.getInstance();

    private static int carAppLoadBalancer = 0;
    private static final List<Integer> carAppAddresses = new ArrayList<>();
    static {
        carAppAddresses.add(3000);
        carAppAddresses.add(3001);
        carAppAddresses.add(3002);
    }

    @Override
    public Response apply(Request request) {
        Integer port;
        synchronized (CarAppProcessor.class) {
            port = carAppAddresses.get(carAppLoadBalancer++);
            carAppLoadBalancer %= carAppAddresses.size();
        }

        try {
            return client.execute(request, port);
        } catch (ConnectException e) {
            synchronized (CarAppProcessor.class) {
                carAppAddresses.remove(port);
                carAppLoadBalancer %= carAppAddresses.size();
                throw new RuntimeException(e);
            }
        }
    }
}
