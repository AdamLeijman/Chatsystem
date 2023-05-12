package controller;

import entity.User;
import java.util.ArrayList;
import java.util.HashMap;

public class Clients {
    private final HashMap<User, ClientThread> clients = new HashMap<>();
    private final ArrayList<User> connectedUsers = new ArrayList<>();

    public synchronized void put(User user, ClientThread client) {
        clients.put(user,client);
    }

    public synchronized ClientThread get(User user) {
        return clients.get(user);
    }

    public synchronized ArrayList<User> activeUsers(){
        return connectedUsers;
    }

    public synchronized void addActiveUser(User user){
        connectedUsers.add(user);
    }

    public synchronized void removeActiveUsers(User user){
        connectedUsers.remove(user);
    }

    public synchronized HashMap<User, ClientThread> getClients() {
        return clients;
    }
}