package service;

import entity.Customer;
import entity.User;
import src.main.java.CryptographyService;
import src.main.java.IAuthentication;
import storage.DataBase;

import javax.crypto.SecretKey;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthenticationService extends UnicastRemoteObject implements IAuthentication {

    public AuthenticationService() throws RemoteException { }

    DataBase dataBase = DataBase.getInstance();

    @Override
    public String signIn(String username, String password) {
        Optional<User> user = dataBase.findUser(username);

        if (user.isEmpty())
            return null;

        String hashedPass = CryptographyService.hashPassword(password);
        boolean valid = CryptographyService.verifyPassword(password, hashedPass);

        if (!valid)
            return null;

        // Token generico
        String token = UUID.randomUUID().toString();
        dataBase.saveSession(token, user.get());

        System.out.println("Sign in: " + username + " -> " + token);

        return token;
    }

    @Override
    public String signUp(String username, String password, String name) {
        String hashedPass = CryptographyService.hashPassword(password);
        User user = new Customer(
                username,
                hashedPass,
                name
        );

        dataBase.saveUser(user);

        System.out.println("Sign up: " + user.getUsername());

        return signIn(username, password);
    }

    @Override
    public void signOut(String token) {
        System.out.println("Sign out: " + token);
        dataBase.deleteSession(token);
    }

    @Override
    public boolean validate(String token) {
        User user = dataBase.getSession(token);

        if (user != null)
            System.out.println("Validate: " + user.getUsername() + " -> true");
        else
            System.out.println("Validate: " + token + " -> false");

        return user != null;
    }

    @Override
    public boolean hasRole(String token, String[] roles) {
        if (!validate(token)) return false;

        User user = dataBase.getSession(token);
        for (String role : roles)
            if (role.equals(user.getRole().toString())) {
                System.out.println("Has role: " + user.getUsername() + ":" + user.getRole().toString() + " -> true");
                return true;
            }

        System.out.println("Has role: " + user.getUsername() + ":" + user.getRole().toString() + " -> false");
        return false;
    }

    @Override
    public SecretKey getHmac(String token) {
        User user = dataBase.getSession(token);
        System.out.println("Get hmac: " + user.getUsername());
        return user.getHmac();
    }

    @Override
    public Map<String, String> getUserData(String token) throws RemoteException {
        User user = dataBase.getSession(token);

        Map<String, String> data = new HashMap<>();
        data.put("name", user.getName());
        data.put("role", user.getRole().toString());

        return data;
    }
}
