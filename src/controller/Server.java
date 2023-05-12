package controller;

import entity.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {
    private final int port;

    public Server(int port) {
        this.port=port;
    }

    public static void main(String[] args) {
        Server server = new Server(1441);
        server.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Client 1");
        new Client("Client 2");
        new Client("Client 3");

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Client 4");
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server: skapad");
            Clients clients = new Clients();
            while (true) {
                Socket socket = serverSocket.accept();
                ClientThread ch = new ClientThread(socket, clients);
                Thread thread = new Thread(ch);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientThread extends Thread {
    public ObjectInputStream is = null;
    public ObjectOutputStream os = null;
    private final Socket clientSocket;
    private final Clients clientsC;

    public ClientThread(Socket clientSocket, Clients clientsC) {
        this.clientSocket = clientSocket;
        this.clientsC = clientsC;
    }
    public void run() {
        try {
            this.is = new ObjectInputStream(clientSocket.getInputStream());
            this.os = new ObjectOutputStream(clientSocket.getOutputStream());

            User user = (User) this.is.readObject();
            clientsC.put(user, this);
            clientsC.addActiveUser(user);

            while(!clientSocket.isClosed()){
                HashMap<User, ClientThread> map = clientsC.getClients();
                Object obj = is.readObject();
                if (obj instanceof Message m) {
                    for (User key : map.keySet()) {
                        map.get(key).os.writeObject(m);
                        map.get(key).os.flush();
                        clientsC.get(key).os.writeObject(clientsC.activeUsers());
                        clientsC.get(key).os.flush();
                    }
                }
            }
            clientsC.removeActiveUsers(user);

        } catch (IOException e) {
            System.out.println("User Session terminated");
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found");
        }
    }
}






