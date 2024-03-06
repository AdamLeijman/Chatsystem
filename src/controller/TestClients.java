package controller;

import javax.swing.*;

public class TestClients {
    public static void main(String[] args)  {
        new Client(0);
        new Client(1);
        new Client(2);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client(3);
    }
}
