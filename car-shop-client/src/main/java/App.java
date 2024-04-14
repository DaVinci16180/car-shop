import menus.Menus;
import network.DiscoveryClient;
import network.LocalStorage;

public class App {

    private static final LocalStorage storage = LocalStorage.getInstance();

    /*
    -> Simule um cliente que tem duas requisições bloqueadas pelo firewall.
        => Sendo uma que tente acessar a base de dados diretamente.
        => E a outra, que tente acessar um Backdoor instalado clandestinamente.
     */

    public static void main(String[] args) {
        while (true) {
            try {
                DiscoveryClient discovery = new DiscoveryClient();
                discovery.discover();

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
