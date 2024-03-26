import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import entity.Administrator;
import entity.Customer;
import entity.User;
import service.AuthenticationService;
import src.main.java.IAuthentication;
import storage.DataBase;

public class App {

    static DataBase db = DataBase.getInstance();

    public static void main(String[] args) {
        try {
            setup();

            AuthenticationService authService = new AuthenticationService();

            UnicastRemoteObject.unexportObject(authService, true);
            IAuthentication remote = (IAuthentication) UnicastRemoteObject.exportObject(
                    authService, 0
            );

            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            LocateRegistry.getRegistry().bind("auth", remote);

            System.out.println("Servidor online.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setup() {
        User admin = new Administrator("admin", "admin", "Administrador");
        User customer = new Customer("joseitamar", "senha123", "José Itamar");

        db.saveUser(admin);
        db.saveUser(customer);
    }
}
