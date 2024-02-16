package entity;

import java.util.*;

public class UnsendMessages {
    private HashMap<User, ArrayList<Message>> unsentMap = new HashMap<>();

    // egna tillägg
    public synchronized void put(User user, Message message) {
        // hämta ArrayList – om null skapa en och placera i unsend
        System.out.println("UnsendMessages: put() meddelande har lagrats");

        if (unsentMap.get(user)==null){
            ArrayList<Message> messageArrayList = new ArrayList<>();
            messageArrayList.add(message);
            unsentMap.put(user, messageArrayList);
            System.out.println("UnsendMessages: message " + message.getText() + " added NEW to unsentMap");
        } else {
            unsentMap.get(user).add(message);
            System.out.println("UnsendMessages: message " + message.getText() + " added to unsentMap");
        }
    }

    public synchronized ArrayList<Message> get(User user) {
        String temp = "Null" + user.getUsername();
        for (Map.Entry<User, ArrayList<Message>> entry : unsentMap.entrySet()) {
            User usr = entry.getKey();
            if (usr.getUsername().equals(temp)) {
                System.out.println("Match found for user: " + user.getUsername());
                return entry.getValue();
            }
        }
        return null;
    }

    public synchronized void clear(){
        unsentMap.clear();
    }

    // fler synchronized-metoder som behövs
    public synchronized ArrayList<Message> deliver(User user){
        // Metod för att leverera alla meddelanden för en viss användare, när de ansluter sig till systemet
        ArrayList<Message> messages = unsentMap.remove(user);
            if(messages != null){
            return new ArrayList<>(messages);
            } else{
                return new ArrayList<>();
        }
    }
}
