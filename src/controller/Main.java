package controller;

import boundary.ServerUI;

import java.util.function.ToDoubleBiFunction;

public class Main {


    public static void main(String[] args) {

        /*To DO list
        1. Projektrapport https://docs.google.com/document/d/1ult885RAV0reZsTqXJDE6Oa735izRSVT/edit?usp=sharing&ouid=115394361315385912023&rtpof=true&sd=true
        2. UML-Diagram
        3. Knapp för att filtrera meddelande-historik mellan två tidpunkter
        */

        Server server = new Server(1441);
        server.start();

        ServerUI serverUI = new ServerUI();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Frederic", 0, "avatars/1.jpeg");
        new Client("John", 1, "avatars/0.png");
        new Client("Josephine", 2, "avatars/1.jpeg");


        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Ewa", 3, "avatars/1.jpeg");
    }
}
