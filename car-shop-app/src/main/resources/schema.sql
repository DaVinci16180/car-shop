CREATE SEQUENCE car_seq
    INCREMENT 1
    START 100;

CREATE TABLE car (
    id integer PRIMARY KEY DEFAULT nextval('car_seq'),
    name varchar NOT NULL,
    registration varchar NOT NULL,
    year integer NOT NULL,
    price numeric NOT NULL,
    category varchar NOT NULL
);

INSERT INTO car (name, registration, year, price, category) VALUES ('Ford Ka', '123978612', 2004, 4500, 'ECONOMIC'),
('Chevrolet Onix', '498274301', 2016, 32025.66, 'ECONOMIC'),
('Hyundai HB20', '943837294', 2020, 56935.20, 'ECONOMIC'),
('Fiat Uno', '019238429', 2007, 5000, 'ECONOMIC'),
('Renault Logan', '123942810', 2019, 67400.30, 'INTERMEDIATE'),
('Toyota Etios', '984172384', 2018, 62670, 'INTERMEDIATE'),
('Chevrolet Prisma', '993882712', 2017, 55500.50, 'INTERMEDIATE'),
('Hyundai HB20s', '782134923', 2023, 92600.40, 'INTERMEDIATE'),
('Honda Civic', '736491284', 2022, 123500, 'EXECUTIVE'),
('Toyota Corolla', '283917428', 2021, 112840, 'EXECUTIVE'),
('Audi A3', '894274102', 2022, 168035.20, 'EXECUTIVE'),
('Chevrolet Cruze', '329817482', 2020, 104200, 'EXECUTIVE');