package boundary;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PnlWriteMessage extends JPanel {
   private MainFrame mainFrame;
    private JButton send, add, image;
    private JTextArea write;
    private JLabel lblIcon;

    public PnlWriteMessage(MainFrame mainFrame) {
        this.mainFrame=mainFrame;
        createSouth();

    }

    private void createSouth() {
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        JPanel pnlButtons = new JPanel();
        add = new JButton("Add contact");
        send = new JButton("Send");
        image = new JButton("Add picture");
        write = new JTextArea("Write message here");
        write.setPreferredSize(new Dimension(400, 20));

        send.addActionListener(l -> {
            updateConversation(false);
        });

        image.addActionListener(l -> {
            updateConversation(true);
        });

        pnlButtons.add(write, BorderLayout.SOUTH);
        pnlButtons.add(send, BorderLayout.SOUTH);
        pnlButtons.add(add, BorderLayout.SOUTH);
        pnlButtons.add(image, BorderLayout.SOUTH);
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


}
