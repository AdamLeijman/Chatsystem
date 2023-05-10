package boundary;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class South extends JPanel implements ActionListener {
   private MainFrame mainFrame;
    private JButton send, add, pic;
    private JTextArea write;
    private JLabel lblIcon;
    private Controller controller;

    public South(MainFrame mainFrame) {
        this.mainFrame=mainFrame;
        createSouth();

    }

    private void createSouth() {
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        JPanel pnlButtons = new JPanel();
        add = new JButton("Add contact");
        send = new JButton("Send");
        pic = new JButton("Add picture");
        write = new JTextArea("Write message here");
        write.setPreferredSize(new Dimension(400, 20));
        pnlButtons.add(write, BorderLayout.SOUTH);
        pnlButtons.add(send, BorderLayout.SOUTH);
        pnlButtons.add(add, BorderLayout.SOUTH);
        pnlButtons.add(pic, BorderLayout.SOUTH);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateConversation(false);
            }
        });

        pic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateConversation(true);
            }
        });
        add(pnlButtons);

    }


    public void updateConversation(Boolean imageAndText)  {
        String currTextMessage = "";
        if (imageAndText) {
            ImageIcon image = null;
            try {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    ImageIcon ii = new ImageIcon(f.getPath());
                    lblIcon = new JLabel();
                    lblIcon.setOpaque(true);
                    lblIcon.setPreferredSize(new Dimension(100, 100));
                    lblIcon.setIcon(ii);
                    Icon icon = new ImageIcon(f.getPath());
                    currTextMessage = write.getText();
                    mainFrame.updateConversation("south", "south", currTextMessage, icon);
                    write.setText("");
                }
            } catch (Exception e) {
            }
        } else {
            currTextMessage = write.getText();
            mainFrame.updateConversation("south", "south", currTextMessage, null);
            write.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
