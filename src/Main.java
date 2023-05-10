import controller.Client;
import controller.Controller;
import controller.Server;

import javax.naming.ldap.Control;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(5555);
        server.startConnection();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Controller controller = new Controller();
        Controller controller1 = new Controller();
    }


}