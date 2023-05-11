package controller;

import controller.Client;
import entity.User;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

public class Clients {
    private HashMap<User, Socket> clients = new HashMap<User, Socket>();
    // egna tillägg
    public synchronized void put(User user, Socket socket) {
        clients.put(user, socket);
    }
    public synchronized Socket get(User user) {
        return clients.get(user);

    }

    public synchronized void remove(User user, Client client){
        clients.remove(user, client);
    }
    // fler synchronized-metoder som behövs
}
