package controller;

import entity.*;

import javax.security.auth.callback.Callback;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Client extends Thread implements Runnable, Callback, Serializable {
    private final int port = 5555;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private InetAddress ip;

    private Socket socket;
    private Thread sendMessageThread;
    private Thread receiveMessageThread;
    private User user;
    private Message message;
    private Controller controller;

    public Client() {
        selectUserInfo();
        newClient();
        //ui.addListener((control.Callback) this);
        controller = new Controller(this);
        message = new Message("testMessage");
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
        //ip = InetAddress.getByName(localHost);
        System.out.println("Client: created");
        try {
            socket = new Socket("127.0.0.1", 5556);
            System.out.println("Client: connected");
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            //TODO Check for unsent messages!
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveMessage();
        receiveMessageThread.start();
        //sendMessage();
        //sendMessageThread().start();
    }


    public void sendMessage() {
        //sendMessageThread = new Thread(this);
        sendMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Client: sendMessage loop is active");
                    oos.writeObject(user);
                    oos.flush();
                    //userSendsMessage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /*
    public void userSendsMessage(String[] str, User receiver) throws IOException {

        Message message = new Message(str[0], new ImageIcon("images/gubbe.jpg"));
        message.setReceiver(receiver);
        System.out.println("Client: userSendsMessage!");
        message.setTimeSent(LocalDateTime.now());
        oos.writeObject(message);
        oos.flush();
        message.clearReceivers();
        //message = new Message("new Message");
    }*/

    public void receiveMessage() {
        receiveMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Client: receiveMessage loop is active");
                    oos.writeObject(user);
                    boolean success = true;
                    /*while (success) {
                        System.out.println("Client: INside while loop");
                        if (ois.readObject() instanceof Clients) {
                            Clients clients = (Clients) ois.readObject();
                            clients.put(user, Client.this);
                            success = false;
                        }
                    }*/
                    //lägg till i client och user clients object

                    while (true) {
                        Object obj = ois.readObject();
                        System.out.println("Client: object received");

                        if (obj instanceof Clients) {
                            Clients clients = (Clients) obj;
                            clients.put(user, Client.this);
                        }
                        System.out.println("R");

                        if (obj instanceof Message) {
                            Message message = (Message) obj;
                            message.setTimeReceived(LocalDateTime.now());
                            //TODO skicka till ui:t!
                            //Här mottags ett meddelande, men lyckas inte få upp det i GUIt
                            //controller.testUpdateGUI(message.getText(), "From user");
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void chooseReceiver(User user) {
        message.setReceiver(user);
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

   /* public void showConnectedUsers() {
        ArrayList<User> connUsers = new ArrayList<>();
        Server s = new Server(8888);
        connUsers = s.getConnectedUsers();
        //TODO skicka till ui:t! Fixa så den hämtar från servern och skapar en ny!
    }
    */

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    //Syftet med addToContacts är att få in en user, hämta alla nuvarande kontakter från textfilen för att inte
    // skriva över nuvarande kontakter och addera summan till textfilen
    public void addToContacts(User newContact) {
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
