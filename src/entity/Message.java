package entity;

import entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private String text;
    private byte[] imageBytes;  // Use byte array to store image data
    private LocalDateTime timeSent = null;
    private LocalDateTime timeReceived = null;
    private User sender;
    private User[] receivers;

    public Message(User user, User[] receivers, String testText, ImageIcon imageIcon) {
        sender = user;
        this.receivers = receivers;
        text = testText;
        if(imageIcon != null) {
            setImageIcon(imageIcon);
        } else {
            imageBytes = null;
        }
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public ImageIcon getImage() {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        return byteToImageIcon(imageBytes);
    }


    public User[] getReceivers() {
        return receivers;
    }

    public void setReceivers(User[] receivers) {
        this.receivers = receivers;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public LocalDateTime getTimeReceived() {
        return timeReceived;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public void setTimeReceived(LocalDateTime timeReceived) {
        this.timeReceived = timeReceived;
    }

    private void setImageIcon(ImageIcon imageIcon) {
        this.imageBytes = imageIconToByteArray(imageIcon);
    }

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
