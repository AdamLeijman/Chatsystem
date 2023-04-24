package entity;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Message implements Serializable {
    private String text;
    private ImageIcon image;
    private ArrayList<User> receivers = new ArrayList<>();
    private LocalDateTime timeSent;
    private LocalDateTime timeReceived;

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

    public ArrayList<User> getReceivers() {
        return receivers;
    }

    public void setReceiver(User receiver) {
        receivers.add(receiver);
    }

    public void clearReceivers(){
        receivers.clear();
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
}
