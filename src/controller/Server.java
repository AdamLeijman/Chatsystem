package controller;

import boundary.ServerUI;
import entity.*;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

public class Server {
    private ArrayList<User> connectedUsers = new ArrayList<>();
    private ArrayList<User> totalUsers = new ArrayList<>();
    private Clients clients;
    private ServerUI serverUI;
    private UnsendMessages unsendMessages;
    private int port;

    public Server(int port) {
        unsendMessages = new UnsendMessages();
        serverUI = new ServerUI(this);
        clients = new Clients();
        this.port = port;
        showTrafficLog();
    }

    public void startConnection() {
        Connection connection = new Connection(port);
        connection.start();
    }

    public synchronized void logTraffic(Message currMessage) {
        //System.out.println("Server: logTraffic method triggered");
        LocalDateTime timeSent = currMessage.getTimeSent();
        LocalDateTime timeReceived = currMessage.getTimeReceived();
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
            System.out.println("Server: logged message at " +timeSent);
            for (int i = 0; i< tempList.size(); i++){
                out.write(tempList.get(i) + "\n");
            }
            out.write(timeSent + " " + timeReceived);
            out.close();
        } catch (IOException e){e.printStackTrace();}
    }

    public void showTrafficLog(){
        ArrayList<Object> tempList = new ArrayList<Object>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader("files/Traffic.txt"));
            String line = bf.readLine();
            while (line != null) {
                tempList.add(line);
                line = bf.readLine();
            }
        }catch (IOException e){e.printStackTrace();}
        Object[] trafficList = tempList.toArray();
        serverUI.addInfo(trafficList);
    }

    public ArrayList<User> getConnectedUsers() {
        return connectedUsers;
    }

    private class Connection extends Thread {
        private int port;
        private ServerSocket serverSocket = null;

        public Connection(int port) {
            this.port = port;
        }

        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server: skapad");
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Server: ny klient ansluten");
                    ClientHandler ch = new ClientHandler(socket);
                    Thread thread = new Thread(ch);
                    thread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private class ClientHandler implements Runnable {
            private ObjectOutputStream oos;
            private ObjectInputStream ois;
            private Socket socket;
            private User specificUser;

            public ClientHandler(Socket socket) {
                this.socket = socket;
                System.out.println("Client has been assigned a ClientHandler (ch)");
            }

            public User getUser(String str){
                for (User u : totalUsers){
                    if (Objects.equals(u.getUsername(), str)){
                        return u;
                    }
                }
                return new User("john", new ImageIcon());
            }

            public boolean isOnline(User usr){
                for (User u : connectedUsers){
                    if (Objects.equals(u.getUsername(), usr.getUsername())){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void run() {
                try {
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());

                    specificUser = (User) ois.readObject();
                    clients.put(specificUser, socket);
                    totalUsers.add(specificUser);
                    connectedUsers.add(specificUser);

                    while(!socket.isClosed()){
                        Message currMessage = (Message) ois.readObject();
                        if (currMessage!=null) {
                            logTraffic(currMessage);
                            sendMessage();
                            ArrayList<String> receivers = currMessage.getReceivers();
                        for (String str : receivers) {
                            System.out.println(str);
                            User receiver = getUser(str);
                            if(isOnline(receiver)){
                                System.out.println("hej4 connectedUsers.size()" + connectedUsers.size());
                                Socket receiverSocket = clients.get(receiver);
                                ObjectOutputStream oos2 = new ObjectOutputStream(receiverSocket.getOutputStream());
                                oos2.writeObject(currMessage);
                            } else {
                                unsendMessages.put(receiver, currMessage);
                                System.out.println("Server: receiver offline, Message stored");
                            }
                        }
                    }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            private void sendMessage() {
                sadasd
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(5556);
        server.startConnection();
    }
}
