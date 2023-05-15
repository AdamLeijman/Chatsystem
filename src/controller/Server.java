package controller;

import entity.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {
    private final int port;
    private ArrayList<User> disconnectedUsers = new ArrayList<>();
    private HashMap<User, ClientThread> clients1 = new HashMap<>();


    public Server(int port) {
        this.port=port;
    }

    public static void main(String[] args) {
        Server server = new Server(1441);
        server.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Client 1");
        new Client("Client 2");
        new Client("Client 3");

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Client("Client 4");
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server: skapad");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientThread ch = new ClientThread(socket);
                new Thread(ch).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class Clients {
        private HashMap<User, ClientThread> clients = new HashMap<>();
        private ArrayList<User> connectedUsers = new ArrayList<>();

        public synchronized void put(User user, Server.ClientThread client) {
            clients.put(user, client);
        }

        public synchronized Server.ClientThread get(User user) {
            return clients.get(user);
        }

        public synchronized ArrayList<User> activeUsers() {
            return connectedUsers;
        }

        public synchronized void addActiveUser(User user) {
            connectedUsers.add(user);
            System.out.println("client " + user);
        }

        public synchronized void removeActiveUsers(User user) {
            connectedUsers.remove(user);
        }

        public synchronized HashMap<User, ClientThread> getClients() {
            return clients;
        }
    }

    public class ClientThread implements Runnable {
        public ObjectInputStream is = null;
        public ObjectOutputStream os = null;
        private Socket clientSocket;
        private User user;
        private Reader reader;
        private Writer writer;

        private ClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        public void run() {
            try {
                this.os = new ObjectOutputStream(clientSocket.getOutputStream());
                this.is = new ObjectInputStream(clientSocket.getInputStream());

                Object obj = is.readObject();
                user = (User) obj;
                clients1.put(user, this);

                writer = new Writer();
                reader = new Reader(writer);
                System.out.println("Connection Est");

            } catch (IOException e) {
                //System.out.println("User Session terminated");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public ObjectOutputStream getOs() {
            return os;
        }

        private class Reader{
            private Writer writer;
            boolean isRunning =  true;

            public Reader(Writer writer) {
                this.writer=writer;

                check4();
            }

            private void check4() {
                try {
                    while (isRunning) {
                        writer.updateConnections();

                      //  if(is.available()>0) {
                            Object obj = is.readObject();
                            if (obj instanceof Message m) {
                                writer.sendCurrMessage(m);
                            }
                            if (obj instanceof String s) {
                                if(s=="close") {
                                    isRunning = false;
                                }
                            }
                       // }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        clientSocket.close();
                        clients1.remove(user);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private class Writer {
            public void updateConnections() throws IOException {
                User[] uList = new User[100];
                int count= 0;
                for (User u : clients1.keySet()) {
                    /*
                    for (User connU: disconnectedUsers){
                        if(connU.getUsername()!=u.getUsername()){
                            uList[count++] = u;
                        }
                    }*/
                    uList[count++] = u;
                }


                Iterator cIterator = clients1.entrySet().iterator();
                while(cIterator.hasNext()){
                    Map.Entry mapElement = (Map.Entry)cIterator.next();
                    //System.out.println("HashMap after adding bonus marks:" + mapElement.getValue());
                    ClientThread ch = (ClientThread) mapElement.getValue();
                    ch.getOs().writeObject(uList);
                }
            }

            public void sendCurrMessage(Message m) throws IOException {
                for (User u : clients1.keySet()) {
                        if (Objects.equals(u.getUsername(), m.getReceivers()[0].getUsername())) {
                            clients1.get(u).os.writeObject(m);
                        }
                }
                clients1.get(user).os.writeObject(m); //skickartill sig själv
                clients1.get(user).os.flush();


            }
            }

        }


}






