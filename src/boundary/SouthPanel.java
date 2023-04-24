package boundary;

import controller.Controller;
import entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class SouthPanel extends JPanel implements ActionListener {

    private MainFrame mainFrame;
    private Controller controller;
    private JButton send, add, pic;
    private JTextArea write;
    private JList<Object> currDialog;
    private Object currMessage;
    private JLabel lblIcon;

    public SouthPanel(MainFrame mainFrame, Controller controller) {
        this.mainFrame = mainFrame;
        this.controller = controller;

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
                displayMessage();
            }

        });

        pic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile();
            }
        });

        add(pnlButtons);
    }

    public void selectFile()  {
        try {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
               ImageIcon ii = new ImageIcon(f.getPath());
                lblIcon = new JLabel();
                lblIcon.setOpaque(true);
                lblIcon.setPreferredSize(new Dimension(100, 100));
                lblIcon.setIcon(ii);
                BufferedImage image = ImageIO.read(f);
                mainFrame.updateDialog(image);
            } else {
            }
        }catch (Exception e){}
    }


    public void displayMessage() {
        currMessage = write.getText();
        mainFrame.updateDialog(currMessage);
        /*currDialog = new JList();
        String[] message = write.getText().split("\n");
        if (!(message.equals(""))) {
            currDialog.setListData(message);

            try {
                mainFrame.getClient().userSendsMessage(message, new User("this is another test user", new ImageIcon("images/new1.jpg")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("SouthPanel: message forwarded");
        }
         */
        write.setText("");
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
