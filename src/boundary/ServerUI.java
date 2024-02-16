package boundary;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class ServerUI {
    private JFrame frame;
    private JPanel south, center;
    private JList<String> info;
    private JFormattedTextField startTimeField, endTimeField;
    private ArrayList<String> tempList; // Declare tempList at the class level
    private JTextField daysField; // Replace endTimeField with daysField
    private String targetDate = "2024-02-16";


    public ServerUI() {
        createUI();
    }

    /**
     * Creates the UI for the server
     */
    public void createUI() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Server UI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLayout(new BorderLayout(10, 10));
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            center = new JPanel();
            center.setPreferredSize(new Dimension(500, 400));
            info = new JList<>();

            JScrollPane scrollPane = new JScrollPane(info);
            scrollPane.setPreferredSize(new Dimension(350, 300));
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            center.add(scrollPane);

            south = new JPanel();
            south.setPreferredSize(new Dimension(100, 100));

            // Add days field
            daysField = new JTextField(5); // Set the preferred size as needed

            // Add filter button
            JButton filterButton = new JButton("Filter");
            filterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filterDate();
                }
            });

            // Add components to the south panel
            south.add(new JLabel("Last N Days:"));
            south.add(daysField); // Replace endTimeField with daysField
            south.add(filterButton);

            tempList = new ArrayList<>();

            frame.add(south, BorderLayout.SOUTH);
            frame.add(center, BorderLayout.CENTER);
            frame.setVisible(true);
            addExistingInfo();
        });
    }

    private void addExistingInfo() {
        BufferedReader bf;
        try {
            bf = new BufferedReader(new FileReader("files/Traffic.txt"));
            String line = bf.readLine();
            while (line != null && !line.startsWith(targetDate)) {
                tempList.add(line);
                line = bf.readLine();
            }
            String[] tempArray = new String[tempList.size()];
            for (int i = 0; i < tempList.size(); i++) {
                tempArray[i] = tempList.get(i);
            }
            info.setListData(tempArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the sent and received time to the file
     * @param sent
     * @param received
     */
    public void addInfo(LocalDateTime sent, LocalDateTime received){
        System.out.println(sent + " " + received    );
        BufferedReader bf;
        ArrayList<String> tempList = new ArrayList<>();
        try {
            bf = new BufferedReader(new FileReader("files/Traffic.txt"));
            String line = bf.readLine();
            while (line != null) {
                tempList.add(line);
                line = bf.readLine();
            }
            BufferedWriter out = new BufferedWriter(new FileWriter("files/Traffic.txt"));
            System.out.println("Server: logged message at " + sent);
            tempList.add(sent + " " + received);
            for (String s : tempList) {
                out.write(s + "\n");
            }
            String newMessage = sent + " " + received + "\n";
            Files.write(Paths.get("files/Traffic.txt"), newMessage.getBytes(), StandardOpenOption.APPEND);

            String[] tempArray = new String[tempList.size()];
            for (int i = 0; i< tempList.size(); i++){
                tempArray[i] = tempList.get(i);
            }
            info.setListData(tempArray);
            out.close();
        } catch (IOException e){e.printStackTrace();}
    }

    private void filterDate() {
        try {
            int lastNDays = Integer.parseInt(daysField.getText());
            LocalDate currentDate = LocalDate.now();
            LocalDate targetDate = currentDate.minusDays(lastNDays);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            this.targetDate = targetDate.format(formatter);
            System.out.println("Target Date set to: " + this.targetDate);
        } catch (NumberFormatException | DateTimeParseException ex) {
            System.err.println("Invalid input for last N days. Please enter a valid number.");
        }
        // Clear history and update JList
        tempList.clear();
        DefaultListModel<String> model = new DefaultListModel<>();
        info.setModel(model);
        addExistingInfo();
    }






}
