import network.Server;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        for (int port = 3000; port <= 3002; port++) {
            try {
                new Thread(new Server(port)).start();
                System.out.println("Servidor iniciado na porta " + port);
                return;
            } catch (IOException e) {
                System.out.println("Porta " + port + " ocupada.");
            }
        }

        System.out.println("Não foi possível iniciar o servidor!");
    }
}
