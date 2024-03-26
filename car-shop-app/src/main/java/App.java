import model.Car;
import network.RSAServer;
import network.Server;
import service.CarService;
import src.main.java.IAuthentication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {

    static CarService carService = CarService.getInstance();

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        setup();

        try {
            new Thread(new Server(8080)).start();
            new Thread(new RSAServer(8081)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setup() {
        Car car1 = new Car();
        car1.setName("Ford Ka");
        car1.setRegistration("123978612");
        car1.setYear(2004);
        car1.setPrice(4500);
        car1.setCategory(Car.Category.ECONOMIC);

        Car car2 = new Car();
        car2.setName("Chevrolet Onix");
        car2.setRegistration("498274301");
        car2.setYear(2016);
        car2.setPrice(32025.66);
        car2.setCategory(Car.Category.ECONOMIC);

        Car car3 = new Car();
        car3.setName("Hyundai HB20");
        car3.setRegistration("943837294");
        car3.setYear(2020);
        car3.setPrice(56935.20);
        car3.setCategory(Car.Category.ECONOMIC);

        Car car4 = new Car();
        car4.setName("Fiat Uno");
        car4.setRegistration("019238429");
        car4.setYear(2007);
        car4.setPrice(5000);
        car4.setCategory(Car.Category.ECONOMIC);

        // ==============================================

        Car car5 = new Car();
        car5.setName("Renault Logan");
        car5.setRegistration("123942810");
        car5.setYear(2019);
        car5.setPrice(67400.30);
        car5.setCategory(Car.Category.INTERMEDIATE);

        Car car6 = new Car();
        car6.setName("Toyota Etios");
        car6.setRegistration("984172384");
        car6.setYear(2018);
        car6.setPrice(62670);
        car6.setCategory(Car.Category.INTERMEDIATE);

        Car car7 = new Car();
        car7.setName("Chevrolet Prisma");
        car7.setRegistration("993882712");
        car7.setYear(2017);
        car7.setPrice(55500.50);
        car7.setCategory(Car.Category.INTERMEDIATE);

        Car car8 = new Car();
        car8.setName("Hyundai HB20s");
        car8.setRegistration("782134923");
        car8.setYear(2023);
        car8.setPrice(92600.40);
        car8.setCategory(Car.Category.INTERMEDIATE);

        // ==============================================

        Car car9 = new Car();
        car9.setName("Honda Civic");
        car9.setRegistration("736491284");
        car9.setYear(2022);
        car9.setPrice(123500);
        car9.setCategory(Car.Category.EXECUTIVE);

        Car car10 = new Car();
        car10.setName("Toyota Corolla");
        car10.setRegistration("283917428");
        car10.setYear(2021);
        car10.setPrice(112840);
        car10.setCategory(Car.Category.EXECUTIVE);

        Car car11 = new Car();
        car11.setName("Audi A3");
        car11.setRegistration("894274102");
        car11.setYear(2022);
        car11.setPrice(168035.20);
        car11.setCategory(Car.Category.EXECUTIVE);

        Car car12 = new Car();
        car12.setName("Chevrolet Cruze");
        car12.setRegistration("329817482");
        car12.setYear(2020);
        car12.setPrice(104200);
        car12.setCategory(Car.Category.EXECUTIVE);

        carService.save(car1);
        carService.save(car2);
        carService.save(car3);
        carService.save(car4);
        carService.save(car5);
        carService.save(car6);
        carService.save(car7);
        carService.save(car8);
        carService.save(car9);
        carService.save(car10);
        carService.save(car11);
        carService.save(car12);
    }
}
