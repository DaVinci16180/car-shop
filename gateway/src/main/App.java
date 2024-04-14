package main;

import main.network.DiscoveryServer;
import main.network.RSAServer;
import main.network.Server;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            new Thread(new Server()).start();
            new Thread(new RSAServer()).start();
            new Thread(new DiscoveryServer()).start();

            System.out.println("Gateway online.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}