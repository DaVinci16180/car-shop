package actions;

import network.Client;
import network.LocalStorage;
import src.main.java.Request;
import src.main.java.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CarActions {

    static LocalStorage storage = LocalStorage.getInstance();
    static Client client = Client.getInstance();

    public static void add(
            String name,
            String registration,
            int year,
            double price,
            int category
    ) {
        Request request = new Request();
        request.setPath("car-admin/add");

        request.addBody("name", name);
        request.addBody("year", year);
        request.addBody("price", price);
        request.addBody("category", category);
        request.addBody("registration", registration);

        client.execute(request, true);
    }

    public static void delete(int id) {
        Request request = new Request();
        request.setPath("car-admin/delete");

        request.addBody("id", id);

        client.execute(request, true);
    }

    public static void update(
            int id,
            String name,
            String registration,
            int year,
            double price,
            int category
    ) {
        Request request = new Request();
        request.setPath("car-admin/update");

        request.addBody("id", id);
        request.addBody("name", name);
        request.addBody("year", year);
        request.addBody("price", price);
        request.addBody("category", category);
        request.addBody("registration", registration);

        client.execute(request, false);
    }

    public static Map<String, Object> details(int id) {
        Request request = new Request();
        request.setPath("car/details");

        request.addBody("id", id);

        Response response = client.execute(request, false);

        return (Map<String, Object>) response.getBody().get("data");
    }

    public static List<Map<String, Object>> list() {
        Request request = new Request();
        request.setPath("car/list");

        Response response = client.execute(request, false);

        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    public static List<Map<String, Object>> search(String query) {
        Request request = new Request();
        request.setPath("car/search");

        request.addBody("query", query);

        Response response = client.execute(request, false);

        return (List<Map<String, Object>>) response.getBody().get("data");
    }
}
