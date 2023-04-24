package boundary;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class EastPanel extends JPanel {
    private JPanel pnlView;
    private MainFrame mainFrame;
    private JList<Object> contacts;
    private JList<Object> connectedUsers;

    public EastPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        pnlView = new JPanel();

        JLabel contactList = new JLabel("Your contacts:");
        contacts = new JList<>();
        contacts.setPreferredSize(new Dimension(130, 350));
        contacts.setBackground(Color.WHITE);
        contacts.setOpaque(true);

        JLabel connUsers = new JLabel("Connected users:");
        connectedUsers = new JList<>();
        connectedUsers.setPreferredSize(new Dimension(130, 350));
        connectedUsers.setBackground(Color.WHITE);
        connectedUsers.setOpaque(true);


        pnlView.add(contactList, BorderLayout.EAST);
        pnlView.add(contacts, BorderLayout.EAST);

        pnlView.add(connUsers, BorderLayout.EAST);
        pnlView.add(connectedUsers, BorderLayout.EAST);

        add(pnlView);
    }

    public JList<Object> getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(Object[] connUsers) {
        connectedUsers.setListData(connUsers);
    }

    public JList<Object> getContacts() {
        return contacts;
    }

    public void setContacts(JList<Object> contacts) {
        this.contacts = contacts;
    }



}
