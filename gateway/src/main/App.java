package main;

import main.server.DiscoveryServer;
import main.server.RSAServer;
import main.server.Server;

public class App {
    public static void main(String[] args) {
        try {
            new Thread(new Server()).start();
            new Thread(new RSAServer()).start();
            new Thread(new DiscoveryServer()).start();

            System.out.println("Gateway online.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}