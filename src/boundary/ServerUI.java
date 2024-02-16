package boundary;

import controller.Server;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ServerUI {

    private JFrame frame;
    private JPanel south, center;
    private Server server;
    private JList<String> info;


    public ServerUI(Server server) {
        this.server = server;
        createUI();
    }

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


            //center.add(info);


            south = new JPanel();
            south.setPreferredSize(new Dimension(100, 100));

            frame.add(south, BorderLayout.SOUTH);
            frame.add(center, BorderLayout.CENTER);
            frame.setVisible(true);
            addExistingInfo();

        });

    }

    private void addExistingInfo() {
        BufferedReader bf= null;
        ArrayList<String> tempList = new ArrayList<String>();
        try {
            bf = new BufferedReader(new FileReader("files/Traffic.txt"));
            String line = bf.readLine();
            while (line!= null) {
                tempList.add(line);
                line = bf.readLine();
            }
            String[] tempArray = new String[tempList.size()];
            for (int i = 0; i< tempList.size(); i++){
               // info.append(tempList.get(i) + "\n");
                tempArray[i] = tempList.get(i);
            }
            info.setListData(tempArray);
        } catch (IOException e){e.printStackTrace();}
    }

    public void addInfo(LocalDateTime received, LocalDateTime sent){
        BufferedReader bf= null;
        ArrayList<String> tempList = new ArrayList<String>();
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
            for (int i = 0; i< tempList.size(); i++){
                out.write(tempList.get(i) + "\n");
            }
            String newMessage = sent + " " + received + "\n";
            Files.write(Paths.get("files/Traffic.txt"), newMessage.getBytes(), StandardOpenOption.APPEND);

            String[] tempArray = new String[tempList.size()];
            for (int i = 0; i< tempList.size(); i++){
                // info.append(tempList.get(i) + "\n");
                tempArray[i] = tempList.get(i);
            }
            info.setListData(tempArray);
            out.close();
        } catch (IOException e){e.printStackTrace();}


    }

}
