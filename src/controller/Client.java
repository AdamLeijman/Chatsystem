package controller;

import boundary.ChatApplicationGUI;
import entity.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;


public class Client {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ChatApplicationGUI view;
    private User user;
    private Socket socket;
    private User[] onlineUsers;
    private int[] currIndexes = null;
    private User[] currChatPartner;
    private String name;
    private final int port = 1441;
    private String myAvatar = "avatars/1.jpeg";

    public Client(String name, int off) {
        this.name =name;
        user = new User(name, null);
        view = new ChatApplicationGUI(this, off, myAvatar);


        try {
            socket = new Socket("127.0.0.1", port);
            System.out.println("Client: connected");
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        receiveMessage();
    }

    public void receiveMessage() {
        Thread receiveMessageThread = new Thread(() -> {
            try {
                while (true) {
                    Object obj = ois.readObject();
                   

                    if (obj instanceof Message m) {
                        /*
                        int c=0;
                        String[] conversationalists = new String[m.getReceivers().cou+1];
                        for (User u : m.getReceivers()) {

                                names.append(u.getUsername());
                        }*/
                        //names.append(m.getSender().getUsername());

                        //currChatPartner = m.getReceivers();

                        m.setTimeSent(LocalDateTime.now());

                        System.out.println("HEY"+ m.getImage());

                        view.incomingMessage(m.getSender().getUsername(),
                                m.getText(), m.getImage(), m.getTimeSent(), m.getTimeReceived());
                    }
                    /*else if (obj instanceof User[] uList) {
                        //if(view.getReceivers().length<1) {
                            onlineUsers = uList;
                            view.setOnline(uList);
                        //}
                    }*/
                    else if (obj instanceof User[] uList) {
                        setOnline(uList);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
            }
            try{
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        receiveMessageThread.start();
    }

    public void newMessage(String sTo[], String text, ImageIcon icon) {

        User me = findUserByUsername(onlineUsers, name);
        User[] sendTo = new User[sTo.length];
        for(int i=0; i<sTo.length;i++){
            sendTo[i] = findUserByUsername(onlineUsers, sTo[i]);
        }

        Message message = new Message(me, sendTo, text, icon);
        System.out.println(message.getImage());
        //Message messageToAll = new Message(user, currChatPartner, recentMessage, (ImageIcon) icon);
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static User findUserByUsername(User[] users, String targetUsername) {
        for (User user : users) {
            if (user.getUsername().equals(targetUsername)) {
                return user;  // Return the matching user directly
            }
        }

        return new User("Null" + targetUsername, null);  // Return null if no match is found
    }


    private void setOnline(User[] uList) {
        if (!Objects.equals(uList, onlineUsers)) {
            ArrayList<String> userNames = new ArrayList<>();
            for (User user : uList) {
                String userName = user.getUsername();
                userNames.add(userName);
            }
            onlineUsers = uList;

            //modify list to hide clients own name & update GUI by setOnline()
            userNames.remove(name);
            JList<String> userNameList = new JList<>(userNames.toArray(new String[0]));
            view.setOnline(userNameList);
        }
    }

    public String getName() {
        return name;
    }

    public void shutDown() {
        try {
            if (socket != null && !socket.isClosed()) {
                // Close ObjectOutputStream first to ensure proper closure
                if (oos != null) {
                    oos.close();
                }

                // Close ObjectInputStream
                if (ois != null) {
                    ois.close();
                }

                // Close the socket
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public void addContact(String[] newContacts) {
        try {
            // Read existing contacts from the file
            Set<String> existingContacts = readExistingContacts();

            // Append new contacts, avoiding duplicates
            for (String contact : newContacts) {
                if (!existingContacts.contains(contact)) {
                    existingContacts.add(contact);
                }
            }

            // Write the updated contacts back to the file
            writeContactsToFile(existingContacts);

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your application's needs
        }
    }

    public Set<String> readExistingContacts() throws IOException {
        Set<String> existingContacts = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("files/contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                existingContacts.add(line);
            }
        }

        return existingContacts;
    }

    private void writeContactsToFile(Set<String> contacts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("files/contacts.txt"))) {
            for (String contact : contacts) {
                writer.write(contact);
                writer.newLine();
            }
        }
    }
}


