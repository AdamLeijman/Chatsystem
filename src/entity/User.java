package entity;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable { // Även användas i strömmar
    private final String username;

    public User(String username, ImageIcon image) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int hashCode() {
        return username.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User)
            return username.equals(((User) obj).getUsername());
        return false;
    }

}