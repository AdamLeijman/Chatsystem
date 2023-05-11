import controller.Client;
import controller.Server;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(5556);
        server.startConnection();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Client client = new Client();
        Client client1 = new Client();
    }


}