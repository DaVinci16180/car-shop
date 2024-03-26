import menus.Menus;
import network.LocalStorage;

public class App {

    private static final LocalStorage storage = LocalStorage.getInstance();

    public static void main(String[] args) {
        while (true) {
            try {
                Menus.main(false);
            } catch (SecurityException e) {
                storage.reset();
                storage.error = true;
                storage.errorMessage = e.getMessage();
            } catch (Exception e) {
                storage.reset();
                storage.error = true;
                storage.errorMessage = "Erro inesperado. Faça login novamente.";
            }
        }
    }
}
