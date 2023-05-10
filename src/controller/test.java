package controller;

import boundary.ServerUI;
import entity.Message;
import entity.User;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class test {

    public static void main(String[] args) throws IOException {

        Server server = new Server(5555);
        server.startConnection();



        for (int i=0; i<1000;i++){}

       // Client client = new Client();
        for (int i=0; i<1000;i++){}



        ServerUI serverUI = new ServerUI(server);

        //Method is working
        /*User user = new User("test", new ImageIcon());
        User user2 = new User("test2", new ImageIcon());
        client.addToContacts(user);
        client.addToContacts(user2);*/



    }
}
