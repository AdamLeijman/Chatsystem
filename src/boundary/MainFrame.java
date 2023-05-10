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
    private PnlDisplayChat pnlDisplayChat;

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
    }

    private void InitializeGUI() {
        pnlWriteMessage = new PnlWriteMessage(this);
        pnlWriteMessage.setPreferredSize(new Dimension(100, 100));
        frame.add(pnlWriteMessage, BorderLayout.SOUTH);

        pnlDisplayChat = new PnlDisplayChat(this);
        pnlDisplayChat.setPreferredSize(new Dimension(700, 500));
        frame.add(pnlDisplayChat);

//        pnlConsumers = new PanelConsumers(controller);
  //      frame.add(pnlConsumers);
    }

    public void updateConversation(String user, String currentReceiver, String recentMessage, Icon recentImage){
        user = client.getUsername();
        System.out.println(user + currentReceiver + recentMessage + recentImage);
        pnlDisplayChat.updateCenterPanel(user, currentReceiver, recentMessage, recentImage);
    }

}
