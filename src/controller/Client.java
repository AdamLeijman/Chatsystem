package controller;

import boundary.MainFrame;
import entity.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Client {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Thread receiveMessageThread;
    private MainFrame view;
    private User user;
    private Socket socket;
    private User[] onlineUsers;
    private int[] currIndexes = null;
    private User[] outMsgreceivers;
    User[] contacts = new User[100];
    private boolean running = true;
    private User[] inMsgReceivers;

    public Client(String name) {
        user = new User(name, new ImageIcon("avatars/1.jpeg"));
        view= new MainFrame( this, name);
        //selectUserInfo();

        int port = 1441;
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
        receiveMessageThread.start();

        addToContacts(null);
    }

    public void selectUserInfo() {
        try {
            String existingUser = null;
            ImageIcon existingAvatar = null;
            if(existingUser == null) {
                BufferedWriter bw = new BufferedWriter(new FileWriter("files/ExistingUser.txt"));
                String name = JOptionPane.showInputDialog(null, "Enter name");
                bw.write(name + "\n");
                ImageIcon avatar = new ImageIcon();
                String[] options = {"0", "1"};
                int choice = JOptionPane.showOptionDialog(null, "Choose avatar", "Choose avatar",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 0) {
                    avatar = new ImageIcon("avatars/0.png");
                    bw.write("avatars/0.png" + "\n");
                } else if (choice == 1) {
                    avatar = new ImageIcon("avatars/1.jpeg");
                    bw.write("avatars/1.jpeg" + "\n");
                }
                bw.close();
                user = new User(name, avatar);
            } else{
                user = new User(existingUser, existingAvatar);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receiveMessage() {
        receiveMessageThread = new Thread(() -> {
            try {
                while (true) {
                    Object obj = ois.readObject();
                   

                    if (obj instanceof Message m) {
                        StringBuilder names = new StringBuilder();
                        for (User u : m.getReceivers()) {
                                names.append(u.getUsername());
                        }
                        names.append(m.getSender().getUsername());

                        inMsgReceivers = m.getReceivers();

                        view.updateConversation(m.getSender().getUsername(), String.valueOf(names), m.getText(), m.getImage());
                    }
                    else if (obj instanceof User[] uList) {
                        if(view.getReceivers().length<1) {
                            onlineUsers = uList;
                            view.showOnline(uList);
                        }
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
    }


    public void addToContacts(int[] indices) { // WOrks only with 1 contact at a time
        User newContact = null;

        if(indices!=null){
            newContact = onlineUsers[indices[0]]; //Takes first index
        }

        ArrayList<String> tempList = readContacts();

        try (BufferedWriter out = new BufferedWriter(new FileWriter("files/Contacts.txt"))) {
            for (String s : tempList) {
                out.write(s + "\n");
            }
            if (newContact!=null) {
                if (!tempList.contains(newContact.getUsername())) {
                    out.write(newContact.getUsername());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view.updateContacts(tempList);
    }

    public ArrayList<String> readContacts(){
        BufferedReader bf;
        ArrayList<String> tempList = new ArrayList<>();
        try {
            bf = new BufferedReader(new FileReader("files/Contacts.txt"));
            String line = bf.readLine();
            while (line != null) {
                tempList.add(line);
                line = bf.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return tempList;
    }


    public void newMessage(String recentMessage, Icon icon) {
        if(outMsgreceivers ==null && view.getReceivers().length<1) {
            JOptionPane.showMessageDialog(null, "SELECT RECEIVER!");
        } else {

            if (view.getReceivers().length >= 1) {
                System.out.println(Arrays.toString(view.getReceivers()) + "Wweewewweweweweew");
                if (currIndexes != view.getReceivers()) {
                    currIndexes = view.getReceivers();
                }
                outMsgreceivers = new User[currIndexes.length];
                int count = 0;
                StringBuilder names = new StringBuilder();
                for (int index : currIndexes) {
                    outMsgreceivers[count++] = onlineUsers[index];
                    names.append(onlineUsers[index].getUsername());
                }

                if(inMsgReceivers!=null) {
                    boolean newChat = true;
                    for (int i = 0; i < outMsgreceivers.length; i++) {
                        for (int j = 0; j < inMsgReceivers.length; j++) {
                            if (Objects.equals(outMsgreceivers[i].getUsername(), inMsgReceivers[j].getUsername())) {
                                newChat = false;
                            }
                        }
                    }
                    if (newChat) {
                        view.updateChattingWithTitle(String.valueOf(names));
                        view.resetChatWindow();
                    }
                }
            }

            Message messageToAll = new Message(user, outMsgreceivers, recentMessage, (ImageIcon) icon);
            try {
                oos.writeObject(messageToAll);
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void disconnect() {
        //running = false;
        String str = "close";
        try {
            oos.writeObject(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
