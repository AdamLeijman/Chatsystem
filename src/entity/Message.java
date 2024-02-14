package entity;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message implements Serializable {
    private String text;
    private ImageIcon image;
    private LocalDateTime timeSent;
    private LocalDateTime timeReceived;
    private User sender;
    private ArrayList<User> activeUsers = new ArrayList<>();
    private User[] receivers;

    public Message(User user, User[] receivers, String testText, ImageIcon imageIcon) {
        sender=user;
        this.receivers=receivers;
        text = testText;
        image = imageIcon;
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public ImageIcon getImage() {
        return image;
    }

    public User[] getReceivers() {
        return receivers;
    }

}
