package entity;

public class Customer extends User {
    public Customer(String username, String password, String name) {
        super(username, password, name, Role.CUSTOMER);
    }
}
