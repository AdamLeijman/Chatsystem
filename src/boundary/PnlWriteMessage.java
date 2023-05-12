package boundary;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PnlWriteMessage extends JPanel {
   private final MainFrame mainFrame;
    private JTextArea write;

    public PnlWriteMessage(MainFrame mainFrame) {
        this.mainFrame=mainFrame;
        createSouth();

    }

    private void createSouth() {
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        JPanel pnlButtons = new JPanel();
        JButton add = new JButton("Add contact");
        JButton send = new JButton("Send");
        JButton image = new JButton("Add picture");
        write = new JTextArea("Write message here");
        write.setPreferredSize(new Dimension(400, 20));
        JButton exit = new JButton("Exit");

        send.addActionListener(l -> updateConversation(false));

        image.addActionListener(l -> updateConversation(true));

        exit.addActionListener(l -> mainFrame.exit());

        pnlButtons.add(write, BorderLayout.SOUTH);
        pnlButtons.add(send, BorderLayout.SOUTH);
        pnlButtons.add(add, BorderLayout.SOUTH);
        pnlButtons.add(image, BorderLayout.SOUTH);
        pnlButtons.add(exit, BorderLayout.SOUTH);

        add(pnlButtons);

    }

    public void updateConversation(Boolean imageAndText)  {
        String currTextMessage;
        if (imageAndText) {
            try {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    ImageIcon ii = new ImageIcon(f.getPath());
                    JLabel lblIcon = new JLabel();
                    lblIcon.setOpaque(true);
                    lblIcon.setPreferredSize(new Dimension(100, 100));
                    lblIcon.setIcon(ii);
                    Icon icon = new ImageIcon(f.getPath());
                    currTextMessage = write.getText();
                    //mainFrame.updateConversation("south", "south", currTextMessage, icon);
                    mainFrame.sendMessage(currTextMessage, icon);
                    write.setText("");
                }
            } catch (Exception ignored) {
            }
        } else {
            currTextMessage = write.getText();
            //mainFrame.updateConversation("south", "south", currTextMessage, null);
            mainFrame.sendMessage(currTextMessage, null);
            write.setText("");
        }
    }


}
