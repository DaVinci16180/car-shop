package repository;

import model.Car;

import java.util.*;
import java.util.stream.Collectors;

public class CarRepository {

    private static int sequenceGenerator = 100;

    private CarRepository() {}

    private static final class InstanceHolder {
        private static final CarRepository instance = new CarRepository();
    }

    public static CarRepository getInstance() {
        return InstanceHolder.instance;
    }

    private HashMap<Integer, Car> cars = new HashMap<>();

    public Car findById(int id) {
        return cars.get(id);
    }

    public Set<Car> findAll() {
        return new HashSet<>(cars.values());
    }

    public Set<Car> findByNameAndRegistration(String query) {
        Set<Car> result = cars
                .values()
                .stream()
                .filter(c -> c.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toSet());

        result.addAll(
            cars
                .values()
                .stream()
                .filter(c -> c.getRegistration().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toSet())
        );

        return result;
    }

    public synchronized Car save(Car car) {
        car.setId(sequenceGenerator++);
        cars.put(car.getId(), car);

        return car;
    }

    public synchronized void delete(int id) {
        cars.remove(id);
    }
}
