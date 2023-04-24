package boundary;

import controller.Client;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame {

    private Controller controller;
    private JFrame frame;
    private JPanel south, east, center;
    private Client client;
    private String user;
    private String currentReceiver;

    public MainFrame(Controller controller, Client client) {
        this.client = client;
        this.controller = controller;
        frame = new JFrame("Chat application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 500);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        south = new SouthPanel(this);
        east = new EastPanel(this);
        center = new CenterPanel();
        south.setPreferredSize(new Dimension(100, 100));
        east.setPreferredSize(new Dimension(500, 300));
        center.setPreferredSize(new Dimension(700, 500));

        frame.add(south, BorderLayout.SOUTH);
        //frame.add(east, BorderLayout.EAST);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);

        Icon icon = new ImageIcon("images/gubbe.jpg");
        updateConversation("User", "Receiver", "textmessage", icon);
    }

    public void updateConversation(String user, String currentReceiver, String recentMessage, Icon recentImage){
        user = client.getUsername();
        System.out.println(user + currentReceiver + recentMessage + recentImage);
        getCenter().updateCenterPanel(user, currentReceiver, recentMessage, recentImage);
    }

    public void updateConnUsers(Object object){
        /*onlineUsers.add(object);
        Object[] o = onlineUsers.toArray();
        getEastPanel().setConnectedUsers(o);*/
    }


    public CenterPanel getCenter() {
        return (CenterPanel) center;
    }
}
