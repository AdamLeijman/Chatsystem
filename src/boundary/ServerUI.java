package boundary;

import controller.Server;
import javax.swing.*;
import java.awt.*;

public class ServerUI {

    private JFrame frame;
    private JPanel south, center;
    private Server server;
    private JList info;


    public ServerUI(Server server) {
        this.server = server;
        createUI();
    }

    public void createUI(){
        frame = new JFrame("Server UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        center = new JPanel();
        center.setPreferredSize(new Dimension(500, 400));
        info = new JList();
        info.setPreferredSize(new Dimension(350, 300));
        center.add(info);



        south = new JPanel();
        south.setPreferredSize(new Dimension(100, 100));

        frame.add(south, BorderLayout.SOUTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void addInfo(Object[] obj){
        info.setListData(obj);
    }

}
