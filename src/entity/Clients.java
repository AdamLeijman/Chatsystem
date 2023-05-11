package entity;

import controller.Client;

import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;

public class Clients {
    private HashMap<User, Socket> clients = new HashMap<User, Socket>();
    // egna tillägg
    public synchronized void put(User user, Socket socket) {
        clients.put(user, socket);
    }
    public synchronized Client get(User user) {
        return get(user);
    }

    public synchronized void remove(User user, Client client){
        clients.remove(user, client);
    }
    // fler synchronized-metoder som behövs
}
