import controller.Controller;
import controller.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(5556);
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