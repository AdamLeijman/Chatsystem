package boundary;

import controller.Client;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame <T> {

    private Controller controller;
    private JFrame frame;
    private JPanel south, east, center;
    //La till denna clienten
    private Client client;
    private ArrayList<T> currDialog = new ArrayList<>();
    private ArrayList<Object> currUsers = new ArrayList<>();


    public MainFrame(Controller controller, Client client) {
        this.client = client;
        this.controller = controller;
        createFrame();
    }


    //La till här också
    public void testUpdateGUI(String userName, String message){
        center.add(new JList<String>());
        //center.testUpdateGUI(userName, message);
    }

    public Client getClient() {return client;}

    public void createFrame() {
        frame = new JFrame("Chat application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 500);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        south = new SouthPanel(this, controller);
        east = new EastPanel(this);
        center = new CenterPanel(this, controller);

        south.setPreferredSize(new Dimension(100, 100));
        east.setPreferredSize(new Dimension(500, 300));
        center.setPreferredSize(new Dimension(700, 500));

        frame.add(south, BorderLayout.SOUTH);
        frame.add(east, BorderLayout.EAST);
        frame.add(center, BorderLayout.CENTER);

        frame.setVisible(true);
    }


    public void updateDialog(T obj){
        currDialog.add((T) (client.getUsername() + " - " + obj));
        T[] o = (T[]) currDialog.toArray();
        getCenterPanel().addToDialog(o);
    }

    public void updateConnUsers(Object object){
        currUsers.add(object);
        Object[] o = currUsers.toArray();
        getEastPanel().setConnectedUsers(o);
    }

    public SouthPanel getSouthPanel() {
        return (SouthPanel) south;
    }
    public CenterPanel getCenterPanel(){return (CenterPanel) center;}
    public EastPanel getEastPanel(){return (EastPanel) east;}
}
