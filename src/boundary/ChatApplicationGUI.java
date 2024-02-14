package boundary;

import controller.Client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

public class ChatApplicationGUI extends JFrame {
    private final JTextArea chatTextArea;
    private final JTextField textInputField;
    private final JList<String> connectedUsersList;
    private final Client client;
    private String conversationalist;
    private JPanel chatPanel;

    public ChatApplicationGUI(Client client, int off) {
        this.client=client;
        // Set up the main frame
        setTitle(client.getName());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int xOffset = 600 * off;
        int yOffset = 300 * off;
        setLocation(xOffset, yOffset);

        // Create components
        chatTextArea = new JTextArea();
        textInputField = new JTextField();
        JButton sendButton = new JButton("Send");
        connectedUsersList = new JList<>(new String[]{"User1", "User2", "User3"}); // Replace with actual user data
        connectedUsersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Set layouts
        setLayout(new BorderLayout());

        // Add components to the frame
        add(createChatPanel(), BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);

        // Add action listener for the Send button
        sendButton.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    private JPanel createChatPanel() {
        chatPanel = new JPanel(new BorderLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Chat Area");
        chatPanel.setBorder(titledBorder);
        chatPanel.add(new JScrollPane(chatTextArea), BorderLayout.CENTER);
        return chatPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Input Area");
        inputPanel.setBorder(titledBorder);
        inputPanel.add(textInputField, BorderLayout.CENTER);

        JScrollPane jsp = new JScrollPane(connectedUsersList);
        jsp.setPreferredSize(new Dimension(150, 150)); // Adjust the width as needed
        inputPanel.add(jsp, BorderLayout.WEST);

        inputPanel.add(createSendButtonPanel(), BorderLayout.EAST);
        resetTextField();
        return inputPanel;
    }

    private JPanel createSendButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton sendButton = new JButton("Send");
        buttonPanel.add(sendButton);
        // Add action listener for the Send button
        sendButton.addActionListener(e -> sendMessage());
        return buttonPanel;
    }

    private void sendMessage() {
        System.out.println("GUI: sendMessage()");
        String[] sendTo = getSelectedUsers();
        String message = textInputField.getText();

        if (sendTo.length > 0 && !message.isEmpty()) {
            setConversationalist(sendTo[sendTo.length-1]);
            chatTextArea.append("You to " + Arrays.toString(sendTo) + ": " + message + "\n");
            client.newMessage(sendTo, message, null);
            resetTextField();
        }
    }

    private void resetTextField() {
        textInputField.setText("Type your message here"); // Clear the text input field
    }

    public void incomingMessage(String sender, String text, ImageIcon image, LocalDateTime timeSent, LocalDateTime timeReceived) {
        setConversationalist(sender);

        // Create Font object with a smaller size
        Font smallFont = chatTextArea.getFont().deriveFont(Font.PLAIN, 10);

        // Format time sent and time received
        String formattedTimeSent = timeSent.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String formattedTimeReceived = timeReceived.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        // Set the font for the time
        chatTextArea.setFont(smallFont);

        // Append the message with formatted time
        chatTextArea.append(sender + " to you: " + text + " (Sent: " + formattedTimeSent + ", Received: " + formattedTimeReceived + ")\n");

        // Reset the font back to its original size
        chatTextArea.setFont(chatTextArea.getFont().deriveFont(Font.PLAIN, 12));

        System.out.println("GUI: incomingMessage()");
    }


    public void setOnline(JList<String> userNames) {
        connectedUsersList.setModel(userNames.getModel());  // Update the model of the JList
        connectedUsersList.repaint();  // Refresh the display
        System.out.println("GUI: setOnline()");
    }

    public String[] getSelectedUsers() {
        return connectedUsersList.getSelectedValuesList().toArray(new String[0]);
    }

    public void setConversationalist(String str) {
        if (!Objects.equals(conversationalist, str)) {
            connectedUsersList.setSelectedValue(str, true); //selects item in the jlist
            TitledBorder titledBorder = (TitledBorder) chatPanel.getBorder();
            titledBorder.setTitle("Chatting with " + str);
            chatPanel.repaint(); // Ensure the changes are reflected
            conversationalist = str;
        }
    }

}
