package controller;

import boundary.ChatApplicationGUI;
import entity.Message;
import entity.User;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

public class Client {
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private final ChatApplicationGUI view;
    private final Socket socket;
    private User[] onlineUsers;
    private final String name;

    /**
     * Constructor for the Client class
     * @param name
     * @param off
     * @param myAvatar
     */
    public Client(String name, int off, String myAvatar) {
        this.name = name;
        User user = new User(name, null);
        view = new ChatApplicationGUI(this, off, myAvatar);

        try {
            int port = 1441;
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

    /**
     * Method to receive messages
     */
    public void receiveMessage() {
        Thread receiveMessageThread = new Thread(() -> {
            try {
                while (true) {
                    Object obj = ois.readObject();

                    if (obj instanceof Message m) {
                        view.incomingMessage(m.getSender().getUsername(),
                                m.getText(), m.getImage(), m.getTimeSent(), m.getTimeReceived());
                    } else if (obj instanceof User[] uList) {
                        setOnline(uList);
                    }
                }
            } catch (IOException | ClassNotFoundException ignored) {
            }
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        receiveMessageThread.start();
    }

    public void newMessage(String[] sTo, String text, ImageIcon icon) {
        User me = findUserByUsername(onlineUsers, name);
        User[] sendTo = Arrays.stream(sTo)
                .map(username -> findUserByUsername(onlineUsers, username))
                .toArray(User[]::new);

        Message message = new Message(me, sendTo, text, icon);
        message.setTimeSent(LocalDateTime.now());

        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to find a user by their username
     * @param users
     * @param targetUsername
     * @return
     */
    private User findUserByUsername(User[] users, String targetUsername) {
        return Arrays.stream(users)
                .filter(user -> user.getUsername().equals(targetUsername))
                .findFirst()
                .orElse(new User("Null" + targetUsername, null));
    }

    private void setOnline(User[] uList) {
        if (!Arrays.equals(uList, onlineUsers)) {
            List<String> userNames = Arrays.stream(uList)
                    .map(User::getUsername)
                    .filter(username -> !username.equals(name))
                    .toList();

            onlineUsers = uList;
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
                oos.writeObject("close");
                oos.close();

                if (ois != null) {
                    ois.close();
                }

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addContact(String[] newContacts) {
        try {
            Set<String> existingContacts = readExistingContacts();
            existingContacts.addAll(Arrays.asList(newContacts));
            writeContactsToFile(existingContacts);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> readExistingContacts() throws IOException {
        Set<String> existingContacts = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("files/contacts.txt"))) {
            reader.lines().forEach(existingContacts::add);
        }

        return existingContacts;
    }

    private void writeContactsToFile(Set<String> contacts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("files/contacts.txt"))) {
            contacts.forEach(contact -> {
                try {
                    writer.write(contact);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
