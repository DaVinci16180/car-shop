package repository;

import model.Car;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CarRepository {

    private static final class InstanceHolder {
        private static final CarRepository instance = new CarRepository();
    }

    public static CarRepository getInstance() {
        return InstanceHolder.instance;
    }

    String driver = "org.postgresql.Driver";
    String url = "jdbc:postgresql://localhost:5432/car-shop";
    String user = "carshopapp";
    String pass = "senha123";

    private CarRepository() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Car findById(int id) {
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM car WHERE id = ?"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Car result = null;
            if (resultSet.next()) {
                result = new Car.Builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .registration(resultSet.getString("registration"))
                        .year(resultSet.getInt("year"))
                        .price(resultSet.getDouble("price"))
                        .category(Car.Category.valueOf(resultSet.getString("category")))
                        .build();
            }

            resultSet.close();
            statement.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Car> findAll() {
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM car"
            );

            Set<Car> result = new HashSet<>();
            while (resultSet.next()) {
                result.add(
                        new Car.Builder()
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .registration(resultSet.getString("registration"))
                            .year(resultSet.getInt("year"))
                            .price(resultSet.getDouble("price"))
                            .category(Car.Category.valueOf(resultSet.getString("category")))
                            .build()
                );
            }

            resultSet.close();
            statement.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Car> findByNameAndRegistration(String query) {
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM car " +
                    "WHERE name like ? " +
                    "OR registration like ?"
            );
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            ResultSet resultSet = statement.executeQuery();

            Set<Car> result = new HashSet<>();
            while (resultSet.next()) {
                result.add(
                        new Car.Builder()
                            .id(resultSet.getInt("id"))
                            .name(resultSet.getString("name"))
                            .registration(resultSet.getString("registration"))
                            .year(resultSet.getInt("year"))
                            .price(resultSet.getDouble("price"))
                            .category(Car.Category.valueOf(resultSet.getString("category")))
                            .build()
                );
            }

            resultSet.close();
            statement.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void save(Car car) {
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO car (name, registration, year, price, category) " +
                    "VALUES (?, ?, ?, ?, ?)"
            );
            statement.setString(1, car.getName());
            statement.setString(2, car.getRegistration());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getCategory().name());
            statement.setInt(3, car.getYear());
            statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void update(Car car) {
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE car SET name = ?, registration = ?, year = ?, price = ?, category = ? " +
                    "WHERE id = ?"
            );
            statement.setString(1, car.getName());
            statement.setString(2, car.getRegistration());
            statement.setDouble(4, car.getPrice());
            statement.setString(5, car.getCategory().name());
            statement.setInt(3, car.getYear());
            statement.setInt(6, car.getId());
            statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void delete(int id) {
        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM car WHERE id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();

            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
