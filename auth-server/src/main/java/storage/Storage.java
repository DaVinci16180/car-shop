package storage;

import entity.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Storage {

    private Storage() {}

    private static final class InstanceHolder {
        private static final Storage instance = new Storage();
    }

    public static Storage getInstance() {
        return Storage.InstanceHolder.instance;
    }

    Set<User> users = new HashSet<>();
    HashMap<String, User> sessions = new HashMap<>();

    public void saveUser(User user) {
        users.add(user);
    }

    public Optional<User> findUser(String username) {
        return users
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public void saveSession(String token, User user) {
        sessions.put(token, user);
    }

    public User getSession(String token) {
        return sessions.get(token);
    }

    public void deleteSession(String token) {
        sessions.remove(token);
    }
}
