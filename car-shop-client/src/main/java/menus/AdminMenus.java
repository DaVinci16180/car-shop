package menus;

import actions.CarActions;
import actions.UserActions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminMenus {
    public static void adminOptions(boolean search) {
        List<Integer> ids = new ArrayList<>();
        String query = null;
        int opt = -1;

        while ((opt < 0 || opt > 2) && !ids.contains(opt)) {
            Menus.clear();

            System.out.print("Pesquisar: ");

            if (search) {
                query = Menus.scanner.nextLine();
            } else {
                System.out.println();
            }

            ids = Menus.list(query);
            System.out.println();

            System.out.println("1 - Pesquisar");
            System.out.println("2 - Adicionar Carro");
            System.out.println();
            System.out.println("Id - Selecionar carro");
            System.out.println();
            System.out.println("0 - Sair");
            System.out.println();
            System.out.print("-> ");

            try {
                opt = Integer.parseInt(Menus.scanner.nextLine());
            } catch (Exception ignored) {}
        }

        switch (opt) {
            case 1 -> adminOptions(true);
            case 2 -> create();
            case 0 -> UserActions.logout();
            default -> details(opt);
        }
    }

    static void details(int id) {
        Map<String, Object> car = CarActions.details(id);
        String name = String.valueOf(car.get("name"));
        String registration = String.valueOf(car.get("registration"));
        String category = String.valueOf(car.get("category"));
        int year = (int) car.get("year");
        double price = (double) car.get("price");
        DecimalFormat df = new DecimalFormat("R$ ###,###,##0.00");


        int opt = -1;
        while (opt < 0 || opt > 2) {
            Menus.clear();

            System.out.println("|------------------------|");
            System.out.println("|      _.--------._      |");
            System.out.println("|    ,'.----------.',    |   Nome: " + name);
            System.out.println("|   /='------------`=\\   |   Ano: " + year);
            System.out.println("| .F_______...._______Y. |   Categoria: " + category);
            System.out.println("| |(_)(_) ______ (_)(_)| |   Renavam: " + registration);
            System.out.println("| (....__| ABCD |__....) |");
            System.out.println("|  | |    ~~~~~~    | |  |   " + df.format(price));
            System.out.println("|  `-'              `-'  |");
            System.out.println("|------------------------|");
            System.out.println();
            System.out.println("1 - Editar");
            System.out.println("2 - Deletar");
            System.out.println();
            System.out.println("0 - Voltar");
            System.out.println();
            System.out.print("-> ");
            try {
                opt = Integer.parseInt(Menus.scanner.nextLine());
            } catch (Exception ignored) {}
        }

        switch (opt) {
            case 1 -> update(id);
            case 2 -> delete(id);
        }
    }

    private static void create() {
        Menus.clear();
        System.out.println();
        System.out.println("Adicionar Carro");
        System.out.println();
        System.out.print("# Nome -> ");
        String name = Menus.scanner.nextLine();

        int year = -1;
        while (year < 0) {
            System.out.print("# Ano -> ");
            try {
                year = Integer.parseInt(Menus.scanner.nextLine());
            } catch (Exception ignored) {}
        }

        double price = -1;
        while (price < 0) {
            System.out.print("# Preço -> ");
            try {
                price = Double.parseDouble(Menus.scanner.nextLine());
            } catch (Exception ignored) {}
        }

        int category = 0;
        while (category < 1 || category > 3) {
            System.out.print("# Categoria (1: Econômico; 2: Intermediário; 3: Executivo) -> ");
            try {
                category = Integer.parseInt(Menus.scanner.nextLine());
            } catch (Exception ignored) {}
        }

        System.out.print("# Renavam -> ");
        String registration = Menus.scanner.nextLine();

        CarActions.add(name, registration, year, price, category);
    }

    private static void update(int id) {
        Menus.clear();
        System.out.println();
        System.out.println("Editar Carro");
        System.out.println();
        System.out.print("# Nome -> ");
        String name = Menus.scanner.nextLine();

        int year = -1;
        while (year < 0) {
            System.out.print("# Ano -> ");
            try {
                String input = Menus.scanner.nextLine();
                if (input.isBlank())
                    year = 0;
                else
                    year = Integer.parseInt(input);
            } catch (Exception e) {
                year = -1;
            }
        }

        double price = -1;
        while (price < 0) {
            System.out.print("# Preço -> ");
            try {
                String input = Menus.scanner.nextLine();
                if (input.isBlank())
                    price = 0;
                else
                    price = Double.parseDouble(input);
            } catch (Exception e) {
                price = -1;
            }
        }

        int category = -1;
        while (category < 0 || category > 3) {
            System.out.print("# Categoria (1: Econômico; 2: Intermediário; 3: Executivo) -> ");
            try {
                String input = Menus.scanner.nextLine();
                if (input.isBlank())
                    category = 0;
                else
                    category = Integer.parseInt(input);
            } catch (Exception e) {
                category = -1;
            }
        }

        System.out.print("# Renavam -> ");
        String registration = Menus.scanner.nextLine();

        CarActions.update(id, name, registration, year, price, category);
    }

    private static void delete(int id) {
        Menus.clear();
        CarActions.delete(id);
    }
}
