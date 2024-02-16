package boundary;

import controller.Client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class ChatApplicationGUI extends JFrame {
    private final JList<Object> chatTextArea;
    private final ArrayList<Object> currConv = new ArrayList<>();
    private final JTextField textInputField;
    private final JList<String> connectedUsersList;
    private JList<String> contactsList = new JList<>(new DefaultListModel<>());
    private final Client client;
    private String conversationalist;
    private JPanel chatPanel;
    private final String myAvatar;
    private String imagePath;

    public ChatApplicationGUI(Client client, int off, String myAvatar) {
        this.myAvatar = myAvatar;
        this.client=client;
        // Set up the main frame
        setTitle(client.getName());
        setSize(600, 400);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int xOffset = 600 * off;
        int yOffset = 300 * off;
        setLocation(xOffset, yOffset);

        // Create components
        chatTextArea = new JList<>();
        // chatTextArea.setEditable(false);
        textInputField = new JTextField();
//        JButton sendButton = new JButton("Send");
//        JButton uploadButton = new JButton("Upload Image");
//        JButton exitButton = new JButton("Exit");
        connectedUsersList = new JList<>(new String[]{"User1", "User2", "User3"}); // Replace with actual user data
        connectedUsersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        contactsList = new JList<>(new String[]{"Cont1", "User2", "User3"}); // Replace with actual user data
        contactsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Set layouts
        setLayout(new BorderLayout());

        // Add components to the frame
        add(createChatPanel(), BorderLayout.CENTER);
        add(createInputPanel(), BorderLayout.SOUTH);
        add(createContactsPanel(), BorderLayout.EAST);

        // Add action listener for the Send button
//        uploadButton.addActionListener(e -> uploadImage());
//        sendButton.addActionListener(e -> sendMessage());

        setVisible(true);
    }

    private void uploadImage() {
        // Create a file chooser
        JFileChooser fileChooser = new JFileChooser();

        // Set the file filter to restrict to certain file types (optional)
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        // Show the file chooser dialog
        int result = fileChooser.showOpenDialog(this);  // "this" refers to the parent component, like a JFrame

        // Check if the user selected a file
        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            java.io.File selectedFile = fileChooser.getSelectedFile();

            // Now you can do something with the selected file, such as getting its path
            String filePath = selectedFile.getAbsolutePath();
            System.out.println("Selected file: " + filePath);
            imagePath = filePath;

            // Perform further operations as needed
            // For example, you might want to display the selected image or upload it to a server
        } else {
            // User canceled the file selection
            System.out.println("File selection canceled");
        }
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

        JScrollPane jspContacts = new JScrollPane(contactsList);
        jsp.setPreferredSize(new Dimension(150, 150)); // Adjust the width as needed
        inputPanel.add(jspContacts, BorderLayout.EAST);

        inputPanel.add(createSendButtonPanel(), BorderLayout.EAST);
        resetTextField();
        return inputPanel;
    }

    private JPanel createSendButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton sendButton = new JButton("Send");
        JButton uploadButton = new JButton("Upload Image");
        JButton exitButton = new JButton("Exit");

        //HEre file path = myAvatar
        ImageIcon icon = new ImageIcon(myAvatar);
        icon.setImage(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        JLabel label = new JLabel(icon);
        buttonPanel.add(label);


        buttonPanel.add(sendButton);
        buttonPanel.add(uploadButton);
        buttonPanel.add(exitButton);
        // Add action listener for the Send button
        sendButton.addActionListener(e -> sendMessage());
        uploadButton.addActionListener(e2 -> uploadImage());
        exitButton.addActionListener(e3 -> {
            // Get the top-level container (JFrame) of the buttonPanel
            SwingUtilities.getWindowAncestor(buttonPanel).dispose();
            client.shutDown();
        });
        return buttonPanel;
    }

    private void sendMessage() {
        System.out.println("GUI: sendMessage()");
        String[] sendTo = getSelectedUsers();
        String message = textInputField.getText();

        if (sendTo.length > 0 && !message.isEmpty()) {
            setConversationalist(sendTo[sendTo.length-1]);
            if (imagePath != null) {
                String messageWithTime = ("You to " + Arrays.toString(sendTo) + ": " + message);
                currConv.add(messageWithTime);
                // Append HTML formatted message with image
                ImageIcon imageIcon = new ImageIcon(imagePath);

                Image a = rescaleMethod(imageIcon);
                // Get the Image object from ImageIcon

                // Create a new ImageIcon from the scaled image
                ImageIcon scaledImageIcon = new ImageIcon(a);
                currConv.add(scaledImageIcon);
            } else {
                currConv.add("You to " + Arrays.toString(sendTo) + ": " + message);
            }
            chatTextArea.setListData(currConv.toArray());
            client.newMessage(sendTo, message, new ImageIcon(imagePath));
            resetTextField();
        }
    }

    private Image rescaleMethod(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();
        int originalWidth = imageIcon.getIconWidth();
        int originalHeight = imageIcon.getIconHeight();

        int maxWidth = 400;
        int maxHeight = 200;

        // Beräkna skalningsfaktor för att bibehålla proportionerna och passa in i de maximala dimensionerna
        double scaleFactor = Math.min((double) maxWidth / originalWidth, (double) maxHeight / originalHeight);

        // Skala bilden med den beräknade skalningsfaktorn
        int scaledWidth = (int) (originalWidth * scaleFactor);
        int scaledHeight = (int) (originalHeight * scaleFactor);

        return image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
    }


    private void resetTextField() {
        //imagePath = null;
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

        if (image != null) {
            String messageWithTime = sender + " to you: " + text + " (Sent: " + formattedTimeSent + ", Received: " + formattedTimeReceived + ")";
            currConv.add(messageWithTime);
            // Append HTML formatted message with image
            Image image2 = rescaleMethod(image);
            currConv.add(new ImageIcon(image2));
        } else {
            // Append the message with formatted time
            currConv.add(sender + " to you: " + text + " (Sent: " + formattedTimeSent + ", Received: " + formattedTimeReceived);
        }
        chatTextArea.setListData(currConv.toArray());

        // Reset the font back to its original size
        chatTextArea.setFont(chatTextArea.getFont().deriveFont(Font.PLAIN, 12));

        System.out.println("GUI: incomingMessage()");
    }


    public void setOnline(JList<String> userNames) {
        connectedUsersList.setModel(userNames.getModel());  // Update the model of the JList
        connectedUsersList.repaint();  // Refresh the display
        System.out.println("GUI: setOnline()");
    }

    public void setContacts() throws IOException {
        Set<String> existingContacts = client.readExistingContacts();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (String contact : existingContacts) {
            listModel.addElement(contact);
        }

        contactsList.setModel(listModel);  // Update the model of the JList
        contactsList.repaint();  // Refresh the display
        System.out.println("GUI: setContacts()");
    }


    public String[] getSelectedUsers() {
        ArrayList<String> selectedUsersList = new ArrayList<>();

        // Add selected users from connectedUsersList
        selectedUsersList.addAll(connectedUsersList.getSelectedValuesList());

        // Add selected users from contactsList
        selectedUsersList.addAll(contactsList.getSelectedValuesList());

        // Convert the List to an array of Strings
        return selectedUsersList.toArray(new String[0]);
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

    private void addContact() {
        String[] sendTo = getSelectedUsers();
        client.addContact(sendTo);


        // Get the existing data from the JList
        ListModel<String> listModel = contactsList.getModel();
        DefaultListModel<String> defaultListModel;

        // Check if the current model is a DefaultListModel
        if (listModel instanceof DefaultListModel) {
            defaultListModel = (DefaultListModel<String>) listModel;
        } else {
            // If not, create a new DefaultListModel and copy existing data
            defaultListModel = new DefaultListModel<>();
            for (int i = 0; i < listModel.getSize(); i++) {
                defaultListModel.addElement(listModel.getElementAt(i));
            }
        }

        // Add each user to the list without duplicates
        for (String user : sendTo) {
            if (!defaultListModel.contains(user)) {
                defaultListModel.addElement(user);
            }
        }

        // Set the model to the JList
        contactsList.setModel(defaultListModel);
    }

    private JPanel createContactsPanel() {
        JPanel contactsPanel = new JPanel(new BorderLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Contacts");
        contactsPanel.setBorder(titledBorder);



        JScrollPane contactsScrollPane = new JScrollPane(contactsList);
        contactsPanel.add(contactsScrollPane, BorderLayout.CENTER);

        JButton addToContactsButton = new JButton("Add to Contacts");
        addToContactsButton.addActionListener(e -> addContact());
        contactsPanel.add(addToContactsButton, BorderLayout.SOUTH);
        try {
            setContacts();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return contactsPanel;
    }

}
