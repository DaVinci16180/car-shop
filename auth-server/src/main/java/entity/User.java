package entity;

import src.main.java.CryptographyService;

import javax.crypto.SecretKey;
import java.util.Objects;

public class User {

    public enum Role {
        ADMIN, CUSTOMER
    }

    private final String username;
    private String password;

    private String name;
    private final Role role;

    private SecretKey hmac;

    protected User(String username, String password, String name, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.hmac = CryptographyService.generateKey("HmacSHA256");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public SecretKey getHmac() {
        return hmac;
    }

    public void setHmac(SecretKey hmac) {
        this.hmac = hmac;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
