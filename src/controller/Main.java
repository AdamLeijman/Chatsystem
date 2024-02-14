package controller;

public class Main {


    public static void main(String[] args) {
        //OBS! JAG HAR LADDAT UPP ETT DOKUMENT BASERAT PÅ RAPPORTMALLEN
        //https://docs.google.com/document/d/1ult885RAV0reZsTqXJDE6Oa735izRSVT/edit?usp=sharing&ouid=115394361315385912023&rtpof=true&sd=true

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
