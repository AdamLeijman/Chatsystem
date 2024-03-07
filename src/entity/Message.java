package entity;

import entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private final String text;
    private byte[] imageBytes;  // Use byte array to store image data
    private LocalDateTime timeSent = null;
    private LocalDateTime timeReceived = null;
    private final User sender;
    private User[] receivers;

    /**
     * Constructor for the message class
     * @param user the sender of the message
     * @param receivers the receivers of the message
     * @param text the text of the message
     * @param imageIcon the image of the message
     */
    public Message(User user, User[] receivers, String text, ImageIcon imageIcon) {
        sender = user;
        this.receivers = receivers;
        this.text = text;
        if(imageIcon != null) {
            setImageIcon(imageIcon);
        } else {
            imageBytes = null;
        }
    }

    /**
     * Getter for the sender of the message
     * @return the sender of the message
     */
    public User getSender() {
        return sender;
    }

    /**
     * Getter for the text of the message
     * @return the text of the message
     */
    public String getText() {
        return text;
    }

    /**
     * Getter for the image of the message
     * @return the image of the message
     */
    public ImageIcon getImage() {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        return byteToImageIcon(imageBytes);
    }

    /**
     * Getter for the receivers of the message
     * @return the receivers of the message
     */
    public User[] getReceivers() {
        return receivers;
    }

    /**
     * Getter for the time sent of the message
     * @return the time sent of the message
     */
    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    /**
     * Getter for the time received of the message
     * @return the time received of the message
     */
    public LocalDateTime getTimeReceived() {
        return timeReceived;
    }

    /**
     * Setter for the time sent of the message
     * @param timeSent the time sent of the message
     */
    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    /**
     * Setter for the time received of the message
     * @param timeReceived the time received of the message
     */
    public void setTimeReceived(LocalDateTime timeReceived) {
        this.timeReceived = timeReceived;
    }

    /**
     * Setter for the image of the message
     * @param imageIcon the image of the message
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
