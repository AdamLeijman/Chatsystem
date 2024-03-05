package boundary;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ServerUI {
    private JFrame frame;
    private JPanel south, center;
    private JList<String> info;
    private LocalDateTime startDateTime = null;
    private LocalDateTime endDateTime = null;
    private JLabel startDateTimeLabel;
    private JLabel endDateTimeLabel;


    public ServerUI() {
        createUI();
    }

    public void createUI() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Server UI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 500);
            frame.setLayout(new BorderLayout(10, 10));
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);

            center = new JPanel();
            center.setPreferredSize(new Dimension(1000, 400));
            info = new JList<>();

            JScrollPane scrollPane = new JScrollPane(info);
            scrollPane.setPreferredSize(new Dimension(900, 300));
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            center.add(scrollPane);

            south = new JPanel();
            south.setPreferredSize(new Dimension(100, 100));

            JButton filterButton = new JButton("Filter");
            filterButton.addActionListener(e -> filter());  // Add ActionListener for the button
            south.add(filterButton);

            JButton reset = new JButton("Reset");
            reset.addActionListener(e -> resetFilter());  // Add ActionListener for the button
            south.add(reset);

            // Initialize JLabels
            startDateTimeLabel = new JLabel("Start Date Time: N/A");
            endDateTimeLabel = new JLabel("End Date Time: N/A");
            south.add(startDateTimeLabel);
            south.add(endDateTimeLabel);

            frame.add(south, BorderLayout.SOUTH);
            frame.add(center, BorderLayout.CENTER);
            frame.setVisible(true);
            addExistingInfo();
        });

    }

    private void addExistingInfo() {
        BufferedReader bf;
        ArrayList<String> tempList = new ArrayList<>();
        try {
            bf = new BufferedReader(new FileReader("files/Traffic.txt"));
            String line = bf.readLine();
            while (line!= null) {

                if (isLineOk(line)) {
                    tempList.add(line);
                }
                line = bf.readLine();
            }
            String[] tempArray = new String[tempList.size()];
            for (int i = 0; i< tempList.size(); i++){
                tempArray[i] = tempList.get(i);
            }
            info.setListData(tempArray);
        } catch (IOException e){e.printStackTrace();}
    }

    private boolean isLineOk(String line) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        try {
            // Extract the first X characters (representing the date) from the line
            String datePart = line.substring(0, 16);
            LocalDateTime rowDate = LocalDateTime.parse(datePart, formatter);

            return (startDateTime==null || rowDate.isAfter(startDateTime))
                    && (endDateTime==null || rowDate.isBefore(endDateTime));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addInfo(LocalDateTime sent, LocalDateTime received, String event) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("files/Traffic.txt", true));
            String newMessage = sent + " " + received + event + "\n";
            out.write(newMessage);
            out.close();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }


    public void filter(){
        String inputDate1 = JOptionPane.showInputDialog("Enter the start date (yyyy-MM-dd):");
        String inputTime1 = JOptionPane.showInputDialog("Enter the start time (HH:mm):");

        String inputDate2 = JOptionPane.showInputDialog("Enter the end date (yyyy-MM-dd):");
        String inputTime2 = JOptionPane.showInputDialog("Enter the end time (HH:mm):");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            startDateTime = LocalDateTime.parse(inputDate1 +'T'+ inputTime1, formatter);
        } catch (Exception e) {
            System.out.println("ServerUI: invalid start date or time");
        }
        try {
            endDateTime = LocalDateTime.parse(inputDate2 +'T'+ inputTime2, formatter);
        } catch (Exception e) {
            System.out.println("ServerUI: invalid end date or time");
        }
        clearAndUpdateWindow();
    }

    private void resetFilter() {
        startDateTime = null;
        endDateTime =null;
        clearAndUpdateWindow();
    }

    private void clearAndUpdateWindow() {
        info.setListData(new String[0]);  // Clear the list data by setting an empty array
        addExistingInfo();

        startDateTimeLabel.setText("Start Date Time: " + startDateTime);
        endDateTimeLabel.setText("End Date Time: " + endDateTime);
    }

}