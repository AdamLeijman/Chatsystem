package controller;

import boundary.ServerUI;
import entity.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

public class Server {
    private ArrayList<User> connectedUsers = new ArrayList<>();
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
            private entity.User senderUser;
            //private Message currMessage;

            public ClientHandler(Socket socket) {
                this.socket = socket;
                System.out.println("Client has been assigned a ClientHandler (ch)");
            }

            @Override
            public void run() {
                try {
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());
                    oos.writeObject("Ok from server");
                    oos.flush();

                    Object object;
                    while(true){
                        object = ois.readObject();
                        if (object instanceof String){
                            System.out.print(object);
                        }
                        if (object instanceof User){
                            System.out.print("USER");
                            clients.put(object);

                        }

                        oos.writeObject(clients);
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                //Test
                    //Message testMessage = new Message("test to receive", new ImageIcon());
                    //oos.writeObject(testMessage);

                    //Object obj = ois.readObject();
                    //if (obj instanceof User) {
                      //  senderUser = (User) obj;
                        //System.out.println("Server: User connected to server");
                    //}

                    //syfte läs in meddelande objekt och skicka det till user om
                    //hen är online annars lagra messageobjektet i unsendmessages-klassen
                    /*while(true){



                        object = ois.readObject();

                        if (object instanceof Client) {
                            connectedUsers.add((Client) object);
                            System.out.println("CLIENT ADDED -----------");
                        }

                        if (object instanceof Message) {
                            oos.writeObject(object);
                            oos.flush();
                        //obj = ois.readObject();
                        //object = (Message) obj;
                        //object = (Message) ois.readObject();

                        System.out.println("hej");
                        logTraffic((Message) object);
                        User receiver = (((Message) object).getReceiver());
                        //System.out.println("hej2" + object.getReceivers());
                           /* for (User currReceiverUser : receivers) {
                            System.out.println("hej3 connectedUsers.size()" + connectedUsers.size());
                            if (connectedUsers.size() == 0) {
                                unsendMessages.put(currReceiverUser, object);
                                System.out.println("Server: receiver offline, Message stored");
                            } else {
                                for (int i = 0; i < connectedUsers.size(); i++) {
                                    if (currReceiverUser == connectedUsers.get(i)) {
                                        System.out.println("Server: receiver online, Message sent");
                                        Client currReceiverClient = clients.get(currReceiverUser);
                                        currReceiverClient.receiveMessage();
                                    } else {
                                        if (i == connectedUsers.size() - 1) {
                                            unsendMessages.put(currReceiverUser, object);
                                        }
                                    }
                                }
                            }
                        }



                        //System.out.println("HEJ");
                    } else if (object instanceof Client) {
                            //clients.put((User) obj, (Client) object);
                            oos.writeObject(clients);
                            oos.flush();
                        }
                    }*/

            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(5555);
        server.startConnection();
    }
}
