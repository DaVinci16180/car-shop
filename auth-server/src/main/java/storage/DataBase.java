package storage;

import entity.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DataBase {

    private DataBase() {}

    private static final class InstanceHolder {
        private static final DataBase instance = new DataBase();
    }

    public static DataBase getInstance() {
        return DataBase.InstanceHolder.instance;
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

    public User saveSession(String token, User user) {
        return sessions.put(token, user);
    }

    public User getSession(String token) {
        return sessions.get(token);
    }

    public void deleteSession(String token) {
        sessions.remove(token);
    }
}
