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
        new Client(0);
        new Client(1);
        new Client(2);


        try {
            Thread.sleep(45000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client(3);
    }
}
