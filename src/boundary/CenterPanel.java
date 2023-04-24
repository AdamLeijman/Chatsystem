package boundary;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CenterPanel <T> extends JPanel implements ActionListener {

    private Controller controller;
    private JPanel grid = new JPanel();
    private JList<T> dialog = new JList<>();
    private MainFrame mainFrame;


    public CenterPanel(MainFrame mainFrame, Controller controller) {
        this.mainFrame = mainFrame;
        this.controller = controller;

        grid.setPreferredSize(new Dimension(700, 350));

        dialog.setPreferredSize(new Dimension(500,350));
        grid.add(dialog, BorderLayout.WEST);

        add(grid);

    }

    public JList<T> getDialog() {
        return dialog;
    }

    public void setDialog(JList<T> dialog) {
        this.dialog = dialog;
    }

    public void addToDialog(T[] obj){
        //dialog.setCellRenderer(cr);
        dialog.setListData(obj);
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
