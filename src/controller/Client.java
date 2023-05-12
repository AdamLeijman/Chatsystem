package controller;

import boundary.MainFrame;
import entity.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client extends Thread implements Runnable {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Thread receiveMessageThread;
    private ArrayList<User> connectedUsersAR = new ArrayList<>();
    private MainFrame view;
    private User user;

    public Client(String name) {
        user = new User(name, new ImageIcon("avatars/1.jpeg"));
        view= new MainFrame( this, name);
        //selectUserInfo();
        newClient();
    }

    public void selectUserInfo() {
        try {
            /*BufferedReader bf = new BufferedReader(new FileReader("files/ExistingUser.txt"));
            String existingUser = bf.readLine();
            ImageIcon existingAvatar = new ImageIcon(bf.readLine());
             */
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

    public void newClient() {
        System.out.println("Client: created");
        try {
            int port = 1441;
            Socket socket = new Socket("127.0.0.1", port);
            System.out.println("Client: connected");
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());

            receiveMessage();
            receiveMessageThread.start();

            oos.writeObject(user);
            oos.flush();
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
                        view.updateConversation(m.getSender().getUsername(), user.getUsername(), m.getText(), m.getImage());
                    }
                    else if (obj instanceof ArrayList<?>) {
                            connectedUsersAR = (ArrayList<User>) obj;
                            view.showOnline(connectedUsersAR);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }



    public void addToContacts(User newContact) {
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
        try (BufferedWriter out = new BufferedWriter(new FileWriter("files/Contacts.txt"))) {
            for (String s : tempList) {
                out.write(s + "\n");
            }
            out.write(newContact.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void newMessage(String recentMessage, Icon icon) {
        Message messageToAll = new Message(user, connectedUsersAR, recentMessage, (ImageIcon) icon);
        try {
            oos.writeObject(messageToAll);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
