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


    public User(String username, ImageIcon image) {
        this.username = username;
        if(image != null) {
            setImageIcon(image);
        } else {
            //System.out.println("User has no image");
        }
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getImage() {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        return byteToImageIcon(imageBytes);
    }

    public int hashCode() {
        return username.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User)
            return username.equals(((User) obj).getUsername());
        return false;
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