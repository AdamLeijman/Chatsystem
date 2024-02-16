package controller;

import boundary.ServerUI;

import java.util.function.ToDoubleBiFunction;

public class Main {


    public static void main(String[] args) {
        //OBS! JAG HAR LADDAT UPP ETT DOKUMENT BASERAT PÅ RAPPORTMALLEN
        //https://docs.google.com/document/d/1ult885RAV0reZsTqXJDE6Oa735izRSVT/edit?usp=sharing&ouid=115394361315385912023&rtpof=true&sd=true

        /*To DO list
        //1. Contaktfönster
        2. Logga tid på hårddisk
        2. Refaktorera klasser
        //2. kommentera text
        4 lägga till användarnamn + bild vid inlogg
         */

        Server server = new Server(1441);
        server.start();

        ServerUI serverUI = new ServerUI(server);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Frederic", 0);
        new Client("John", 1);
        new Client("Josephine", 2);


        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Ewa", 3);
    }
}
