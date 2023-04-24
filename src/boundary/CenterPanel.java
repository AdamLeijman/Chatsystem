package boundary;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CenterPanel <T> extends JPanel implements ActionListener {

    private Controller controller;
    private JPanel grid = new JPanel();
    private JList<T> jlistDialog = new JList<>();
    private MainFrame mainFrame;


    public CenterPanel(MainFrame mainFrame, Controller controller) {
        this.mainFrame = mainFrame;
        this.controller = controller;

        grid.setPreferredSize(new Dimension(700, 350));

        jlistDialog.setPreferredSize(new Dimension(500,350));
        grid.add(jlistDialog, BorderLayout.WEST);

        add(grid);

    }

    public JList<T> getJlistDialog() {
        return jlistDialog;
    }

    public void setJlistDialog(JList<T> jlistDialog) {
        this.jlistDialog = jlistDialog;
    }

    public void addToDialog(T[] obj){

        //jlistDialog.setListData(obj);
        String[] str = {"test", "test2"};
        jlistDialog.setListData((T[]) str);

        T[] icon = obj;
        //ScaledImage(icon.getImage(), lblPhotoField.getWidth(), lblPhotoField.getHeight());

        ArrayList<Icon> cars = new ArrayList<Icon>(); // Create an ArrayList object

        //Icon icon = new ImageIcon("images/gubbe.jpg");
        //Icon ic
        //cars.add(new ImageIcon("images/gubbe.jpg"));

        icon[0] = (T) new ImageIcon("images/gubbe.jpg");

        jlistDialog.setListData(icon);
    }

   /* public void paint(Graphics g) {
        g.drawImage( image, 0, 0, null);
    }
    */

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JButton) e.getSource()).getText();
    }



}
