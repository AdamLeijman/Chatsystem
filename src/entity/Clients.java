package entity;

import controller.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Clients implements Serializable {
    private HashMap<User, Client> clients = new HashMap<User, Client>();
    // egna tillägg
    public synchronized void put(User, Client) {
        clients.put(user,client);
    }
    public synchronized User get(entity.User user) {
        return get(user);
    }

    public synchronized void remove(entity.User user, User client){
        clients.remove(user, client);
    }

    public synchronized String[] getList(){
        String[] temp = new String[100];
        int count = 0;
        for(Map.Entry<entity.User, User> set : clients.entrySet()){
            temp[count] = set.getKey().getUsername();
            count++;
        }
        return temp;
    }

    public synchronized entity.User[] getUsers(){
        entity.User[] temp = new entity.User[10];
        int count = 0;
        for(Map.Entry<entity.User, User> set : clients.entrySet()){
            temp[count] = set.getKey();
            System.out.println("----------" + temp[count].getUsername());
            count++;
        }
        return temp;
    }
    // fler synchronized-metoder som behövs
}
