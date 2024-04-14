package main;

import main.network.RSAServer;
import main.network.Server;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            new Thread(new Server(8080)).start();
            new Thread(new RSAServer(8081)).start();

            System.out.println("Gateway online.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}