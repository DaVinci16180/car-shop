package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class DiscoveryClient {

    private final ServerSocket serverSocket;

    public static String serverAddress;
    public static int serverPort;
    public static int rsaServerPort;

    public DiscoveryClient() throws IOException {
        serverSocket = new ServerSocket(0);
    }

    public void discover() throws InterruptedException {
        Thread fetch = new Thread(() -> {
            try {
                Socket server = serverSocket.accept();

                ObjectInputStream in = new ObjectInputStream(server.getInputStream());
                String addr = (String) in.readObject();

                DiscoveryClient.serverAddress = addr.split(";")[0];
                DiscoveryClient.serverPort = Integer.parseInt(addr.split(";")[1]);
                DiscoveryClient.rsaServerPort = Integer.parseInt(addr.split(";")[2]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        fetch.start();

        try {
            // Broadcast para descobrir serviços na rede
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            String serverSocketAddr = serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort();
            byte[] myAddr = serverSocketAddr.getBytes();

            InetAddress broadcast = NetworkInterface
                    .getByInetAddress(InetAddress.getLocalHost())
                    .getInterfaceAddresses()
                    .get(0)
                    .getBroadcast();

            // Broadcasting para a porta específica
            DatagramPacket sendPacket = new DatagramPacket(
                    myAddr,
                    myAddr.length,
                    broadcast,
                    8082
            );

            socket.send(sendPacket);
            System.out.println("Broadcast enviado para descoberta de serviços na rede.");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fetch.join();
    }
}
