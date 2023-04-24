package boundary;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CenterPanel extends JPanel implements ActionListener {
    private final ArrayList<Object> currConversation = new ArrayList<>();
    private final JList<Object> jlistConversation = new JList<>();

    public CenterPanel() {
        jlistConversation.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jlistConversation.setLayoutOrientation(JList.VERTICAL);
        jlistConversation.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(jlistConversation);
        listScroller.setPreferredSize(new Dimension(350, 350));
        JPanel grid = new JPanel();
        grid.setPreferredSize(new Dimension(700, 350));
        grid.add(listScroller, BorderLayout.WEST);
        add(grid);
    }

    public void updateCenterPanel(String user, String currentReceiver, String recentMessage, Icon recentImage){
        currConversation.add(user + " - " + recentMessage);
        if (recentImage!=null) {
            currConversation.add(recentImage);
        }
        Object[] listData = currConversation.toArray();
        jlistConversation.setListData(listData);
    }


    @Override
    public void actionPerformed(ActionEvent e) {}
}
