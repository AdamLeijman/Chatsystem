package controller;

public class Main {


    public static void main(String[] args) {
        Server server = new Server(1441);
        server.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Frederic", 0);
        new Client("John", 1);
        new Client("Josephine", 2);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Ewa", 3);
    }
}
