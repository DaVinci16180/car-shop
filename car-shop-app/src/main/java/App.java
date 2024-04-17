import network.Server;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            new Thread(new Server()).start();
        } catch (IOException e) {
            System.out.println("Não foi possível iniciar o servidor!");
        }
    }
}
