package entity;

import controller.Client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public synchronized String[] getList(){
        String[] temp = new String[100];
        int count = 0;
        for(Map.Entry<User, Client> set : clients.entrySet()){
            temp[count] = set.getKey().getUsername();
            count++;
        }
        return temp;
    }

    public synchronized User[] getUsers(){
        User[] temp = new User[10];
        int count = 0;
        for(Map.Entry<User, Client> set : clients.entrySet()){
            temp[count] = set.getKey();
            System.out.println("----------" + temp[count].getUsername());
            count++;
        }
        return temp;
    }
    // fler synchronized-metoder som behövs
}
