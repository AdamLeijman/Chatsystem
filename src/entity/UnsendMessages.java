package entity;

import java.util.*;

public class UnsendMessages {
    private HashMap<User, ArrayList<Message>> unsentMap = new HashMap<>();


    // egna tillägg
    public synchronized void put(User user, Message message) {
        // hämta ArrayList – om null skapa en och placera i unsend
        System.out.println(message.getText() + "AAA" + message.getReceivers());

        //map.get("vishal")
        if (unsentMap.get(user)==null){
            ArrayList<Message> messageArrayList = new ArrayList<>();
            unsentMap.put(user, messageArrayList);
            System.out.println("UnsendMessages: message " + message.getText() + " added to NEW unsentMap");
        } else {
            unsentMap.get(user).add(message);
            //unsentMap.put(user, unsentMap.get(user).add(message));
            System.out.println("UnsendMessages: message " + message.getText() + " added to unsentMap");
        }


        //ArrayList<Message> messageList = unsentMap.get(user);

        //messageList.add(message);

        //message.getText();
//        ArrayList<Message> messages = unsend.get(user);
  //  if(message == null) {
    //    messages = new ArrayList<>();
      //  unsend.put(user, messages);
   // }
        // lägga till Message i ArrayList
     //   messages.add(message);
    }

    public synchronized ArrayList<Message> get(User user) {
        // Returnera en kopia av ArrayList för att förhindra oavsiktlig ändring av listan
        ArrayList<Message> messages = unsentMap.get(user);
        if(messages == null){
            return new ArrayList<>(messages);
        } else{
            return new ArrayList<>();
        }
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
