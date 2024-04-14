package service;

import repository.CarRepository;
import model.Car;

import java.util.*;

public class CarService {

    private CarService() {}

    private static final class InstanceHolder {
        private static final CarService instance = new CarService();
    }

    public static CarService getInstance() {
        return CarService.InstanceHolder.instance;
    }

    CarRepository carRepository = CarRepository.getInstance();

    public Map<String, Object> findById_convertToMap(int id) {
        Car car = carRepository.findById(id);
        return carToMap(car);
    }

    public List<Map<String, Object>> findAll_convertToMap() {
        Set<Car> cars = carRepository.findAll();
        return carSetToMap(cars);
    }

    public List<Map<String, Object>> findByNameAndRegistration_convertToMap(String query) {
        Set<Car> cars = carRepository.findByNameAndRegistration(query);
        return carSetToMap(cars);
    }

    private List<Map<String, Object>> carSetToMap(Set<Car> cars) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Car car : cars) {
            result.add(carToMap(car));
        }

        return result;
    }

    private Map<String, Object> carToMap(Car car) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", car.getId());
        result.put("name", car.getName());
        result.put("year", car.getYear());
        result.put("price", car.getPrice());
        result.put("category", car.getCategory().getValue());
        result.put("registration", car.getRegistration());

        return result;
    }
}
