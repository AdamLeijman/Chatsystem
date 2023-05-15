package boundary;

import controller.Client;
import entity.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame {
    private JFrame frame;
    private Client client;
    private PnlWriteMessage pnlWriteMessage;
    private PnlChat pnlChat;
    private PnlContacts pnlContacts;
    private String name;

    public MainFrame(Client client, String name) {
        this.client = client;
        this.name=name;

        frame = new JFrame();
        frame.setBounds(0, 0, 1000, 526);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
        frame.setTitle("Chat application " + name);

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
        pnlChat.updateCenterPanel(user, currentReceiver, recentMessage, recentImage);
    }

    public void sendMessage(String recentMessage, Icon icon){
        client.newMessage(recentMessage, icon);
    }

    public void showOnline(User[] connectedUsersAR) {
        pnlContacts.setConnectedUsers(connectedUsersAR);
    }

    public void exit() {
        frame.dispose();
        client.disconnect();
    }

    public int[] getReceivers() {
        return pnlContacts.getReceivers();
    }


    public void updateChattingWithTitle(String receivers) {
        frame.setTitle("Chat application " + name + " Chatting with: " + receivers);
        pnlChat.resetChat();
    }

    public void addContact() {
        client.addToContacts(pnlContacts.getReceivers());
    }

    public void updateContacts(ArrayList<String> values) {
        pnlContacts.updateContacts(values);
    }
}
