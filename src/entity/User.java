package entity;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable { // Även användas i strömmar
    private String username;
    private ImageIcon image;
    private ArrayList<User> contacts;

    public User(String username, ImageIcon image) {
        this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getImage() {
        return image;
    }

    public int hashCode() {
        return username.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User)
            return username.equals(((User) obj).getUsername());
        return false;
    }

    @Override
    public String toString() {
        return "User: " + username +
                ", image=" + image;
    }
}