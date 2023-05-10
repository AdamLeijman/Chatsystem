package controller;

import boundary.MainFrame;
import entity.*;

import javax.security.auth.callback.Callback;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class User extends Thread implements Runnable, Callback, Serializable {
    private final int port = 5555;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private InetAddress ip;

    private Socket socket;
    private Thread sendMessageThread;
    private Thread receiveMessageThread;
    private entity.User user;
    private Message message;
    private MainFrame view;
    private Clients clients;

    public User() {
        view = new MainFrame(this);
        clients = new Clients();
        selectUserInfo();
        receiveMessage();
        receiveMessageThread.start();
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
                user = new entity.User(name, avatar);
            } else{
                user = new entity.User(existingUser, existingAvatar);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void userSendsMessage(String user, String currentReceiver, String recentMessage, Icon recentImage) throws IOException {

        entity.User userCURR = null;
        entity.User[] users = clients.getUsers();
        for(entity.User u : users){
            if(currentReceiver.equals(u.getUsername())) {
                userCURR = u;
                break;
            }
        }
        Message message = new Message(recentMessage, (ImageIcon) recentImage, userCURR);
        //message.setReceiver(userCURR);
        System.out.println("Current receiver "+currentReceiver);
        System.out.println(user);
        System.out.println("_---------------------");
        System.out.println(message.getText());
        System.out.println(message.getImage());
        System.out.println(message.getReceiver().getUsername());

        message.setTimeSent(LocalDateTime.now());
        oos.writeObject(message);
        oos.flush();
        //message.clearReceivers();
        //message = new Message("new Message");
    }

    public void receiveMessage() {
        receiveMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("127.0.0.1", 5556);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos.writeObject(user);
                    oos.flush();

                    Object object;
                    while(true){
                        object = ois.readObject();
                        if (object instanceof String){
                            System.out.print(object);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                /*try {
                    oos.writeObject("OK from Client");
                    while (true) {



                        Object obj = ois.readObject();
                        System.out.println("Client: object received");

                        if (obj instanceof Clients) {
                            clients = (Clients) obj;
                            clients.put(user, Client.this);
                            view.updateConnUsers(clients.getList());
                            System.out.println("!!!!!");
                        }
                        System.out.println("R");

                        if (obj instanceof Message) {
                            Message message = (Message) obj;
                            message.setTimeReceived(LocalDateTime.now());
                            //TODO skicka till ui:t!
                            //if(message.getReceivers().contains(user)) {
                                view.receiveMessage(user.getUsername(), "hej", message.getText(), message.getImage());
                            //}
                            System.out.println(user.getUsername() +"THIS USER");
                            System.out.println(message.getText());
                            System.out.println(message.getImage());
                            System.out.println(message.getReceiver().getUsername());


                            //Här mottags ett meddelande, men lyckas inte få upp det i GUIt
                            //controller.testUpdateGUI(message.getText(), "From user");
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }*/
            }
        });
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void chooseReceiver(entity.User user) {
        message.setReceiver(user);
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    /*public void showConnectedUsers() {
        ArrayList<User> connUsers = new ArrayList<>();

        connUsers = s.getConnectedUsers();
        //TODO skicka till ui:t! Fixa så den hämtar från servern och skapar en ny!
    }

     */


    //Syftet med addToContacts är att få in en user, hämta alla nuvarande kontakter från textfilen för att inte
    // skriva över nuvarande kontakter och addera summan till textfilen
    public void addToContacts(entity.User newContact) {
        BufferedReader bf = null;
        ArrayList<String> tempList = new ArrayList<String>();
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
            for (int i = 0; i < tempList.size(); i++) {
                out.write(tempList.get(i) + "\n");
            }
            out.write(newContact.getUsername());
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
