package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PnlChat extends JPanel {
    private final ArrayList<Object> currConversation = new ArrayList<>();
    private JList<Object> jlistConversation = new JList<>();
    private MainFrame mainFrame;
    private Object[] listData;

    public PnlChat(MainFrame mainFrame) {
        this.mainFrame=mainFrame;
        createPanel();
    }

    private void createPanel() {
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

    public void updateCenterPanel(String user, String currentReceivers, String recentMessage, Icon recentImage){
        currConversation.add(user + " - " + recentMessage);
        if (recentImage!=null) {
            currConversation.add(recentImage);
        }
        listData = currConversation.toArray();
        jlistConversation.setListData(listData);
    }

    public void resetChat() {
        currConversation.clear();
        listData = currConversation.toArray();
        jlistConversation.setListData(listData);
    }
}
