package controller;

import boundary.ChatApplicationGUI;
import entity.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


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

    public Client(String name, int off) {
        this.name =name;
        user = new User(name, new ImageIcon("avatars/1.jpeg"));
        view = new ChatApplicationGUI(this, off);


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

                        view.incomingMessage(m.getSender().getUsername(), m.getText(), m.getImage());
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

    public void newMessage(String sTo[], String text, Icon icon) {

        User me = findUserByUsername(onlineUsers, name);
        User[] sendTo = new User[sTo.length];
        for(int i=0; i<sTo.length;i++){
            sendTo[i] = findUserByUsername(onlineUsers, sTo[i]);
        }

        //Message messageToAll = new Message(user, currChatPartner, recentMessage, (ImageIcon) icon);
        try {
            oos.writeObject(new Message(me, sendTo, text, (ImageIcon) icon));
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

        return null;  // Return null if no match is found
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
}


