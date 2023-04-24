package entity;

import controller.Client;

import java.io.Serializable;
import java.util.HashMap;

public class Clients implements Serializable {
    private HashMap<User, Client> clients = new HashMap<User, Client>();
    // egna tillägg
    public synchronized void put(User user,Client client) {
        clients.put(user,client);
    }
    public synchronized Client get(User user) {
        return get(user);
    }

    public synchronized void remove(User user, Client client){
        clients.remove(user, client);
    }
    // fler synchronized-metoder som behövs
}
