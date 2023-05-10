package boundary;

import controller.Client;
import controller.Controller;

import javax.swing.*;
import java.awt.*;

public class MainFrame {
    private Controller controller;
    private JFrame frame;
    private Client client;
    private PnlWriteMessage pnlWriteMessage;
    private PnlChat pnlChat;
    private PnlContacts pnlContacts;

    public MainFrame(Controller controller, Client client) {
        this.controller = controller;
        this.client = client;

        frame = new JFrame();
        frame.setBounds(0, 0, 1000, 526);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        frame.setTitle("Chat application");

        InitializeGUI();					// Fill in components

        frame.setVisible(true);
        frame.setResizable(false);			// Prevent user from change size
        frame.setLocationRelativeTo(null);
    }

    private void InitializeGUI() {
        pnlWriteMessage = new PnlWriteMessage(this);
        pnlWriteMessage.setPreferredSize(new Dimension(100, 100));
        frame.add(pnlWriteMessage, BorderLayout.SOUTH);

        pnlChat = new PnlChat(this);
        pnlChat.setPreferredSize(new Dimension(700, 500));
        frame.add(pnlChat, BorderLayout.CENTER);

        pnlContacts = new PnlContacts(this);
        pnlContacts.setPreferredSize(new Dimension(500, 300));
        frame.add(pnlContacts, BorderLayout.EAST);
    }

    public void updateConversation(String user, String currentReceiver, String recentMessage, Icon recentImage){
        user = client.getUsername();
        System.out.println(user + currentReceiver + recentMessage + recentImage);
        pnlChat.updateCenterPanel(user, currentReceiver, recentMessage, recentImage);
    }

}
