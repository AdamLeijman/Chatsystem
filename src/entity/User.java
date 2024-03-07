package entity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable { // Även användas i strömmar
    private final String username;
    private byte[] imageBytes;  // Use byte array to store image data

    /**
     * Constructor for the user class
     * @param username the username of the user
     * @param image the users profile image
     */
    public User(String username, ImageIcon image) {
        this.username = username;
        if(image != null) {
            setImageIcon(image);
        } else {
            System.out.println("User has no image");
        }
    }

    /**
     * Getter for the username of the user
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the image of the user
     * @return the image of the user
     */
    public ImageIcon getImage() {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        return byteToImageIcon(imageBytes);
    }

    /**
     * Gets the hashCode for the user
     * @return the hashCode of the user
     */
    public int hashCode() {
        return username.hashCode();
    }

    /**
     * Checks if the username is equal to another username
     * @param obj the object to check
     * @return true if the user is equal to the other user, else false
     */
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User)
            return username.equals(((User) obj).getUsername());
        return false;
    }

    /**
     * Setter for the image of the user
     * @param imageIcon the new image of the user
     */
    private void setImageIcon(ImageIcon imageIcon) {
        this.imageBytes = imageIconToByteArray(imageIcon);
    }

    /**
     * Converts an image icon to a byte array
     * @param icon the icon to convert
     * @return the byte array of the icon
     */
    private byte[] imageIconToByteArray(ImageIcon icon) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return new byte[0];  // Return an empty byte array if ImageIcon is null or has invalid dimensions
        }

        try {
            BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];  // Handle the IOException by returning an empty byte array
        }
    }

    /**
     * Converts a byte array to an image icon
     * @param bytes the byte array to convert
     * @return the image icon of the byte array
     */
    private ImageIcon byteToImageIcon(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(bais);
            return new ImageIcon(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}