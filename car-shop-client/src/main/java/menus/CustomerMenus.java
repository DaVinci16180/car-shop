package menus;

import actions.CarActions;
import actions.UserActions;
import network.LocalStorage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerMenus {
    private static final LocalStorage storage = LocalStorage.getInstance();

    public static void customerOptions(boolean search) {
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
            case 1 -> customerOptions(true);
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
        while (opt < 0 || opt > 1) {
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
            System.out.println("1 - Comprar");
            System.out.println("0 - Voltar");
            System.out.println();
            System.out.print("-> ");
            try {
                opt = Integer.parseInt(Menus.scanner.nextLine());
            } catch (Exception ignored) {}
        }

        if (opt == 1 && storage.token == null) {
            Menus.login();
        }

        if (opt == 1) {
            purchase(id, name);
        }
    }

    private static void purchase(int id, String name) {
        int in = 0;
        while (in != id) {
            Menus.clear();
            System.out.println();
            System.out.print("Digite " + id + " para concluir a operação -> ");
            try {
                in = Integer.parseInt(Menus.scanner.nextLine());
            } catch (Exception ignored) {}
        }

        UserActions.purchaseCar(id);
        successfulPurchase(name);
    }

    private static void successfulPurchase(String name) {
        Menus.clear();
        System.out.println();
        System.out.println("✅ Transação efetuada com sucesso! Aproveite seu novo " + name + "!");

        try {
            Thread.sleep(3000);
        } catch (Exception ignored) {}
    }
}
