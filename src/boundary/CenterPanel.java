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
        MyCellRenderer cr = new MyCellRenderer();
        dialog.setCellRenderer(cr);
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

    class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
        final static ImageIcon longIcon = new ImageIcon("long.gif");
        final static ImageIcon shortIcon = new ImageIcon("short.gif");

        // This is the only method defined by ListCellRenderer.
        // We just reconfigure the JLabel each time we're called.

        public Component getListCellRendererComponent(
                JList<?> list,           // the list
                Object value,            // value to display
                int index,               // cell index
                boolean isSelected,      // is the cell selected
                boolean cellHasFocus)    // does the cell have focus
        {
            String s = value.toString();
            setText(s);
            setIcon((s.length() > 10) ? longIcon : shortIcon);
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }

}
