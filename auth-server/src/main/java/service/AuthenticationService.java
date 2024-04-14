package service;

import entity.Customer;
import entity.User;
import src.main.java.CryptographyService;
import src.main.java.IAuthentication;
import storage.Storage;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AuthenticationService extends UnicastRemoteObject implements IAuthentication {

    public AuthenticationService() throws RemoteException { }

    Storage storage = Storage.getInstance();

    @Override
    public String signIn(String username, String password) throws AccessDeniedException {
        Optional<User> userOpt = storage.findUser(username);

        if (userOpt.isEmpty()) {
            System.out.println("Usuário ou senha inválidos.");
            throw new AccessDeniedException("Usuário ou senha inválidos.");
        }

        User user = userOpt.get();

        if (user.getLoginCooldown() != null && user.getLoginCooldown().isAfter(LocalDateTime.now())) {
            System.out.println(
                    "Tente novamente em " +
                    LocalDateTime.now().until(user.getLoginCooldown(), ChronoUnit.SECONDS) +
                    " segundos."
            );
            throw new AccessDeniedException(
                    "Tente novamente em " +
                    LocalDateTime.now().until(user.getLoginCooldown(), ChronoUnit.SECONDS) +
                    " segundos."
            );
        }

        user.setLoginCooldown(null);

        boolean valid = CryptographyService.verifyPassword(password, user.getPassword());

        if (!valid) {
            user.incrementFailedLoginAttempts();
            System.out.println("Usuário ou senha inválidos.");
            throw new AccessDeniedException("Usuário ou senha inválidos.");
        }

        user.setFailedLoginAttempts(0);

        // Token generico
        String token = UUID.randomUUID().toString();
        storage.saveSession(token, user);

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

        storage.saveUser(user);

        System.out.println("Sign up: " + user.getUsername());

        try {
            return signIn(username, password);
        } catch (AccessDeniedException ignored) {
            return null;
        }
    }

    @Override
    public void signOut(String token) {
        System.out.println("Sign out: " + token);
        storage.deleteSession(token);
    }

    @Override
    public boolean validate(String token) {
        User user = storage.getSession(token);

        if (user != null)
            System.out.println("Validate: " + user.getUsername() + " -> true");
        else
            System.out.println("Validate: " + token + " -> false");

        return user != null;
    }

    @Override
    public boolean hasRole(String token, String[] roles) {
        if (!validate(token)) return false;

        User user = storage.getSession(token);
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
        User user = storage.getSession(token);
        System.out.println("Get hmac: " + user.getUsername());
        return user.getHmac();
    }

    @Override
    public Map<String, String> getUserData(String token) throws RemoteException {
        User user = storage.getSession(token);

        Map<String, String> data = new HashMap<>();
        data.put("name", user.getName());
        data.put("role", user.getRole().toString());

        return data;
    }

    @Override
    public boolean checkSign(String sign, Object message, String token) throws RemoteException {
        User user = storage.getSession(token);
        String generatedSign = CryptographyService.sign(message, user.getHmac());
        return generatedSign.equals(sign);
    }
}
