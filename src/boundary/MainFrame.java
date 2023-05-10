package boundary;

import controller.Client;
import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class MainFrame {

    private Controller controller;
    private JFrame frame;
    private JPanel south, east, center;
    private Client client;
    private String user;
    private String currentReceiver;
    private South pnlWriteMessage;
    private Center pnlDisplayChat;

    public MainFrame(Controller controller, Client client) {
        this.controller = controller;
        this.client = client;

        frame = new JFrame();
        frame.setBounds(0, 0, 730, 526);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        frame.setTitle("Chat application");

        InitializeGUI();					// Fill in components

        frame.setVisible(true);
        frame.setResizable(false);			// Prevent user from change size
        frame.setLocationRelativeTo(null);

        /*frame = new JFrame("Chat application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 730, 526);
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
        updateConversation("User", "Receiver", "textmessage", icon);*/
    }

    private void InitializeGUI() {
        pnlWriteMessage = new South(controller);
        frame.add(pnlWriteMessage, BorderLayout.SOUTH);

        pnlDisplayChat = new Center(controller);
        frame.add(pnlDisplayChat);

//        pnlConsumers = new PanelConsumers(controller);
  //      frame.add(pnlConsumers);
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


    public Center getCenter() { //
        return (Center) center;
    }
}
