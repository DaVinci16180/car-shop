package controller;

import network.annotations.Controller;
import network.annotations.Path;
import service.CarService;
import src.main.java.Request;
import src.main.java.Response;

@Controller(path = "car")
public class CarController {

    private CarController() {}

    private static class InstanceHolder {
        private static final CarController instance = new CarController();
    }

    public static CarController getInstance() {
        return CarController.InstanceHolder.instance;
    }

    private final CarService carService = CarService.getInstance();

    @Path(value = "details", _public = true)
    public Object findOne(Request request) {
        int id = (int) request.getBody().get("id");

        var car = carService.findById_convertToMap(id);

        Response response = Response.success();
        response.addBody("data", car);

        return response;
    }

    @Path(value = "list", _public = true)
    public Object findAll(Request request) {
        var cars = carService.findAll_convertToMap();

        Response response = Response.success();
        response.addBody("data", cars);

        return response;
    }

    @Path(value = "search", _public = true)
    public Object search(Request request) {
        String query = String.valueOf(request.getBody().get("query"));

        var cars = carService.findByNameAndRegistration_convertToMap(query);

        Response response = Response.success();
        response.addBody("data", cars);

        return response;
    }
}
