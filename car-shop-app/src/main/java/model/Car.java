package model;

public class Car {

    public enum Category {
        ECONOMIC("Econômico"),
        INTERMEDIATE("Intermediário"),
        EXECUTIVE("Executivo");

        private final String value;

        Category(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private int id;
    private String name;
    private String registration;
    private int year;
    private double price;
    private Category category;

    public Car() {}

    public Car(Builder builder) {
        this.name = builder.name;
        this.year = builder.year;
        this.price = builder.price;
        this.category = builder.category;
        this.registration = builder.registration;
    }

    public static class Builder {
        private String name;
        private String registration;
        private int year;
        private double price;
        private Category category;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder registration(String registration) {
            this.registration = registration;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        return id == car.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
