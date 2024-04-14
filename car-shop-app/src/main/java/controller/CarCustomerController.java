package controller;

import network.annotations.Controller;
import network.annotations.Path;
import repository.CarRepository;
import src.main.java.Request;
import src.main.java.Response;

@Controller(path = "car-customer")
public class CarCustomerController {

    private static class InstanceHolder {
        private static final CarCustomerController instance = new CarCustomerController();
    }

    public static CarCustomerController getInstance() {
        return CarCustomerController.InstanceHolder.instance;
    }

    CarRepository carRepository = CarRepository.getInstance();

    private CarCustomerController() {}

    @Path("buy")
    public synchronized Object buy(Request request) {
        int id = (int) request.getBody().get("id");
        carRepository.delete(id);

        return Response.success();
    }
}
