package controller;

import entity.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

public class Server extends Thread {
    private final int port;
    private Clients newClients = new Clients();
    private final UnsendMessages unsendMessages = new UnsendMessages();

    public Server(int port) {
        this.port = port;
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

        public synchronized void put(User user, ClientThread client) {
            clients.put(user, client);
        }

        public synchronized ClientThread remove(User user) {
            return clients.remove(user);
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
        private User[] connectedUsers;

        private ClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                this.os = new ObjectOutputStream(clientSocket.getOutputStream());
                this.is = new ObjectInputStream(clientSocket.getInputStream());

                Object obj = is.readObject();
                user = (User) obj;
                newClients.put(user, this);

                writer = new Writer();


                reader = new Reader(writer);
                System.out.println("Connection Est");

            } catch (IOException e) {
                //System.out.println("User Session terminated");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public synchronized ObjectOutputStream getOs() {
            return os;
        }

        private class Reader {
            private Writer writer;
            boolean isRunning = true;

            public Reader(Writer writer) {
                this.writer = writer;

                new Thread(writer).start();
                reading();
            }

            private synchronized void reading() {
                try {
                    while (isRunning) {

                        Object obj = is.readObject();
                        if (obj instanceof Message m) {
                            LocalDateTime date = LocalDateTime.now();
                            m.setTimeReceived(date);
                            writeToU
                            writer.sendCurrMessage(m);
                        }

                        if (obj instanceof String s) {
                            if (s.equals("close")) {
                                isRunning = false;
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        newClients.remove(user);
                        clientSocket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private class Writer implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        updateConnections();
                        checkUnsentMessages();
                        sleep(5000);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            private synchronized void checkUnsentMessages() throws IOException {
                ArrayList<Message> arrayList = unsendMessages.get(user);
                if (arrayList != null) {
                    unsendMessages.clear();
                    for (Message m : arrayList) {
                        getOs().writeObject(m);
                        getOs().flush();
                    }
                }
            }

            public void updateConnections() throws IOException {
                Set<User> clientSet = newClients.getClients().keySet();
                User[] uList = new User[clientSet.size()];
                int count = 0;

                for (User u : clientSet) {
                    uList[count++] = u;
                }

                if (connectedUsers == null || !areArraysEqual(connectedUsers, uList)) {

                    Iterator cIterator = newClients.getClients().entrySet().iterator();
                    while (cIterator.hasNext()) {
                        Map.Entry mapElement = (Map.Entry) cIterator.next();
                        //System.out.println("HashMap after adding bonus marks:" + mapElement.getValue());
                        ClientThread ch = (ClientThread) mapElement.getValue();
                        ch.getOs().writeObject(uList);
                    }
                    connectedUsers = uList;
                }
            }

            public void sendCurrMessage(Message m) throws IOException {
                for (User user : m.getReceivers()) {
                    if (user.getUsername().startsWith("Null")) {
                        unsendMessages.put(user, m);
                    } else {
                        for (User u : newClients.getClients().keySet()) {
                            if (Objects.equals(u.getUsername(), user.getUsername())) {
                                newClients.getClients().get(u).os.writeObject(m);
                            }
                        }
                        newClients.getClients().get(user).os.flush();
                    }
                }
            }

        }


        public boolean areArraysEqual(User[] array1, User[] array2) {
            if (array1.length != array2.length) {
                return false;
            }
            for (User user : array1) {
                if (!Arrays.asList(array2).contains(user)) {
                    return false;
                }
            }
            return true;
        }


    }
}






