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

    public Message(String text, ImageIcon image) {
        this.text = text;
        this.image = image;
    }

    //Syftet med denna konstruktorn är att användare ska kunna skapa messageobjekt utan bilder, dvs textmeddelande
    public Message(String text) {
        this.text = text;
    }
    //Syftet med denna konstruktorn är att användare ska kunna skapa messageobjekt utan text, dvs bildmeddelande
    public Message(ImageIcon image){this.image = image;}

    public Message(User user, User[] receivers, String testText, ImageIcon imageIcon) {
        sender=user;
        this.receivers=receivers;
        text = testText;
        image = imageIcon;
    }



    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }




    public String getText() {
        return text;
    }

    public ImageIcon getImage() {
        return image;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public LocalDateTime getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(LocalDateTime timeReceived) {
        this.timeReceived = timeReceived;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", image=" + image +
                ", sender=" + sender +
                ", receivers=" + receivers +
                '}';
    }

    public ArrayList<User> getActiveUsers() {
        return activeUsers;
    }


    public User[] getReceivers() {
        return receivers;
    }

    public void setReceivers(User[] receivers) {
        this.receivers = receivers;
    }
}
