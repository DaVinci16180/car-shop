package entity;

public class Administrator extends User {
    public Administrator(String username, String password, String name) {
        super(username, password, name, Role.ADMIN);
    }
}
