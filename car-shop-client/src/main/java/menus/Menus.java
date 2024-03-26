package menus;

import actions.CarActions;
import actions.UserActions;
import network.LocalStorage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class Menus {

    static final Scanner scanner = new Scanner(System.in);
    private static final LocalStorage storage = LocalStorage.getInstance();

    public static void main(boolean search) {
        while (true) {
            mainOptions(search);
        }
    }

    private static void mainOptions(boolean search) {
        if (storage.token == null) {
            defaultOptions(false);
        } else if (storage.userRole.equals("ADMIN")) {
            AdminMenus.adminOptions(false);
        } else if (storage.userRole.equals("CUSTOMER")) {
            CustomerMenus.customerOptions(false);
        }
    }

    private static void defaultOptions(boolean search) {
        List<Integer> ids = new ArrayList<>();
        String query = null;
        int opt = -1;

        while ((opt < 1 || opt > 3) && !ids.contains(opt)) {
            clear();

            System.out.print("Pesquisar: ");

            if (search) {
                query = scanner.nextLine();
            } else {
                System.out.println();
            }

            ids = list(query);
            System.out.println();

            System.out.println("1 - Pesquisar");
            System.out.println();
            System.out.println("2 - Login");
            System.out.println("3 - Cadastro");
            System.out.println();
            System.out.println("Id - Selecionar carro");
            System.out.println();
            System.out.print("-> ");

            try {
                opt = Integer.parseInt(scanner.nextLine());
            } catch (Exception ignored) {}
        }

        switch (opt) {
            case 1 -> defaultOptions(true);
            case 2 -> login();
            case 3 -> register();
            default -> CustomerMenus.details(opt);
        }
    }

    private static void details(int id) {
        if (storage.userRole.equals("ADMIN"))
            AdminMenus.details(id);
    }

    private static void userData() {
        if (storage.token == null) {
            System.out.println();
        } else {
            div("Bem-Vindo(a) " + storage.userName, 40, Alignment.RIGHT);
            System.out.println();
        }
    }

    static void login() {
        clear();
        System.out.println();
        System.out.println("Login");
        System.out.println();
        System.out.print("# Usuário -> ");
        String username = scanner.nextLine();
        System.out.print("# Senha -> ");
        String password = scanner.nextLine();

        // Apenas para depuração
        if (storage.hmac == null)
            UserActions.configureHmac(username, password);

        UserActions.login(username, password);
    }

    private static void register() {
        clear();
        System.out.println();
        System.out.println("Cadastro");
        System.out.println();
        System.out.print("# Nome -> ");
        String name = scanner.nextLine();
        System.out.print("# Nome de Usuário -> ");
        String username = scanner.nextLine();
        System.out.print("# Senha -> ");
        String password = scanner.nextLine();

        UserActions.register(name, username, password);
    }

    static List<Integer> list(String filter) {
        System.out.println();

        List<Map<String, Object>> cars;
        if (filter == null || filter.isBlank())
            cars = CarActions.list();
        else
            cars = CarActions.search(filter);

        cars.sort(Comparator.comparing(car -> String.valueOf(car.get("name"))));

        System.out.println(cars.size() + " resultado(s) encontrado(s).");
        for (Map<String, Object> car : cars) {
            row(car);
        }

        return cars.stream().map(c -> (Integer) c.get("id")).toList();
    }

    private static void row(Map<String, Object> car) {
        int id = (int) car.get("id");
        String name = String.valueOf(car.get("name"));
        String registration = String.valueOf(car.get("registration"));
        String category = String.valueOf(car.get("category"));
        int year = (int) car.get("year");
        double price = (double) car.get("price");
        DecimalFormat df = new DecimalFormat("R$ ###,###,##0.00");

        System.out.println(" ------------- ");
        System.out.println("|   Id: "+ id +"   |");
        System.out.println("|  .-'--`-._  | " + name + " - " + year);
        System.out.println("|  '-O---O--' | " + df.format(price));
        System.out.println("|             | ");
        System.out.println(" ------------- ");
        System.out.println();
    }

    private static void sign() {
        System.out.print("""
                
                   ---------------------------.
                 `/""\""/""\""/|""|'|""||""|   ' \\.
                 /    /    / |__| |__||__|      |    ____           ____  _                \s
                /----------=====================|   / ___|__ _ _ __/ ___|| |__   ___  _ __ \s
                | \\  /V\\  /    _.               |  | |   / _` | '__\\___ \\| '_ \\ / _ \\| '_ \\\s
                |()\\ \\W/ /()   _            _   |  | |__| (_| | |   ___) | | | | (_) | |_) |
                |   \\   /     / \\          / \\  |   \\____\\__,_|_|  |____/|_| |_|\\___/| .__/\s
                =C========C==_| ) |--------| ) _/                                    |_|   \s
                 \\_\\_/__..  \\_\\_/_ \\_\\_/ \\_\\_/
                """);
    }

    static void clear() {
        System.out.println("grep=cls");
        error();
        sign();
        userData();
    }

    private static void error() {
        if (storage.error) {
            div("Erro - " + storage.errorMessage, 50, Alignment.CENTER);
            storage.resetError();
        }

        System.out.println();
    }

    public enum Alignment { LEFT, CENTER, RIGHT }

    public static void div(String text, int size, Alignment alignment) {
        if (text.length() >= size - 2) {
            System.out.print(" " + text.substring(0, size - 2) + " ");
            return;
        }

        int spaces = size - text.length() - 2;

        if (spaces == 1)
            alignment = Alignment.LEFT;

        switch (alignment) {
            case LEFT -> {
                System.out.print(" " + text);
                System.out.format(String.format("%1$" + spaces + "s ", ""));
            }
            case CENTER -> {
                System.out.format(String.format(" %1$" + Math.floor(spaces / 2.) + "s", ""));
                System.out.print(text);
                System.out.format(String.format("%1$" + Math.ceil(spaces / 2.) + "s ", ""));
            }
            case RIGHT -> {
                System.out.format(String.format(" %1$" + spaces + "s", ""));
                System.out.print(text + " ");
            }
        }
    }

    public static void repeat(char ch, int times, boolean br) {
        System.out.print(String.format("%1$" + times + "s", "").replace(' ', ch));
        if (br)
            System.out.println();
    }

    public static void padding(int size) {
        System.out.format("%1$" + size + "s", "");
    }

    private static BigDecimal readNumber() {
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (Exception ignored) {
                System.out.print("Erro - insira um número válido -> ");
            }
        }
    }
}
