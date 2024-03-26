package controller;

import network.annotations.Controller;
import network.annotations.Path;
import repository.CarRepository;
import src.main.java.IAuthentication;
import src.main.java.Request;
import src.main.java.Response;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Controller(path = "car-customer")
public class CarCustomerController {

    private static class InstanceHolder {
        private static final CarCustomerController instance = new CarCustomerController();
    }

    public static CarCustomerController getInstance() {
        return CarCustomerController.InstanceHolder.instance;
    }

    CarRepository carRepository = CarRepository.getInstance();
    IAuthentication auth;

    private CarCustomerController() {
        try {
            auth = (IAuthentication) LocateRegistry
                    .getRegistry(Registry.REGISTRY_PORT)
                    .lookup("auth");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Path("buy")
    public Object buy(Request request) {
        try {
            String token = (String) request.getHeaders().get("token");
            if (!auth.hasRole(token, new String[] { "CUSTOMER" }))
                return Response.fail();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        int id = (int) request.getBody().get("id");
        carRepository.delete(id);

        return Response.success();
    }
}
