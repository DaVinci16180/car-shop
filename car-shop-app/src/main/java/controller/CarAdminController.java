package controller;

import model.Car;
import network.annotations.Controller;
import network.annotations.Path;
import repository.CarRepository;
import src.main.java.Request;
import src.main.java.Response;

@Controller(path = "car-admin")
public class CarAdminController {

    private static class InstanceHolder {
        private static final CarAdminController instance = new CarAdminController();
    }

    public static CarAdminController getInstance() {
        return InstanceHolder.instance;
    }

    CarRepository carRepository = CarRepository.getInstance();

    private CarAdminController() {}

    @Path(value = "add")
    public Object addCar(Request request) {
        String name = String.valueOf(request.getBody().get("name"));
        String registration = String.valueOf(request.getBody().get("registration"));
        int year = (int) request.getBody().get("year");
        int category = (int) request.getBody().get("category");
        double price = (double) request.getBody().get("price");

        Car car = new Car.Builder()
                .name(name)
                .registration(registration)
                .year(year)
                .price(price)
                .build();

        switch (category) {
            case 1 -> car.setCategory(Car.Category.ECONOMIC);
            case 2 -> car.setCategory(Car.Category.INTERMEDIATE);
            case 3 -> car.setCategory(Car.Category.EXECUTIVE);
        }

        carRepository.save(car);

        return Response.success();
    }

    @Path(value = "delete")
    public Object deleteCar(Request request) {
        int id = (int) request.getBody().get("id");
        carRepository.delete(id);

        return Response.success();
    }

    @Path(value = "update")
    public Object updateCar(Request request) {
        int id = (int) request.getBody().get("id");
        int year = (int) request.getBody().get("year");
        int category = (int) request.getBody().get("category");
        String name = (String) request.getBody().get("name");
        String registration = (String) request.getBody().get("registration");
        double price = (double) request.getBody().get("price");

        Car car = carRepository.findById(id);

        if (year > 0)
            car.setYear(year);

        if (category > 0)
            switch (category) {
                case 1 -> car.setCategory(Car.Category.ECONOMIC);
                case 2 -> car.setCategory(Car.Category.INTERMEDIATE);
                case 3 -> car.setCategory(Car.Category.EXECUTIVE);
            }

        if (!name.isBlank())
            car.setName(name);

        if (!registration.isBlank())
            car.setRegistration(registration);

        if (price > 0)
            car.setPrice(price);

        carRepository.update(car);

        return Response.success();
    }
}
