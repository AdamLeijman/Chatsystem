package controller;

import boundary.ServerUI;
import entity.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

public class Server extends Thread {
    private final int port;
    private final Clients newClients = new Clients();
    private final UnsendMessages unsendMessages = new UnsendMessages();
    private final ServerUI serverUI = new ServerUI();

    public Server(int port) {
        this.port = port;
    }

    /**
     * The run method creates a server socket and listens for incoming connections. When a connection is established, a new
     */
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

    /**
     * The Clients class is a synchronized class that contains a HashMap of User and ClientThread objects. The put method
     * adds a new User and ClientThread to the HashMap. The remove method removes a User and ClientThread from the HashMap.
     */
    public class Clients {
        private final HashMap<User, ClientThread> clients = new HashMap<>();

        public synchronized void put(User user, ClientThread client) {
            clients.put(user, client);
        }

        public synchronized void remove(User user) {
            clients.remove(user);
        }

        public synchronized HashMap<User, ClientThread> getClients() {
            return clients;
        }
    }

    /**
     * The ClientThread class is a Runnable class that handles the communication with a client. The run method creates an
     * ObjectOutputStream and ObjectInputStream for the client socket. It then reads a User object from the client and adds
     * the User and ClientThread to the newClients HashMap. It then creates a new Writer object and a new Reader object.
     */
    public class ClientThread implements Runnable {
        public ObjectInputStream is = null;
        public ObjectOutputStream os = null;
        private final Socket clientSocket;
        private User user;
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

                serverUI.addInfo(LocalDateTime.now(),
                        null,
                        ", Login from " + user.getUsername());

                writer = new Writer();

                new Reader(writer);
                System.out.println("Connection Est");

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public synchronized ObjectOutputStream getOs() {
            return os;
        }

        private class Reader {
            private final Writer writer;
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
                            m.setTimeReceived(LocalDateTime.now());


                            writer.sendCurrMessage(m);
                        }

                        if (obj instanceof String s) {
                            if (s.equals("close")) {
                                isRunning = false;

                                serverUI.addInfo(LocalDateTime.now(),
                                        null,
                                        ", Logout from " + user.getUsername());
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

        /**
         * The Writer class is a Runnable class that sends messages to the clients. The run method calls the updateConnections
         * and checkUnsentMessages methods every 5 seconds. The updateConnections method sends the connectedUsers array to all
         * clients if it has changed. The checkUnsentMessages method sends any unsent messages to the receivers.
         */
        private class Writer implements Runnable {
            /**
             * The run method calls the updateConnections and checkUnsentMessages methods every 5 seconds.
             */
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

            /**
             * Checks if there are any unsent messages and sends them
             * @throws IOException
             */
            private synchronized void checkUnsentMessages() throws IOException {
                ArrayList<Message> arrayList = unsendMessages.get(user);
                if (arrayList != null) {
                    unsendMessages.clear();
                    for (Message m : arrayList) {

                        m.setTimeReceived(LocalDateTime.now()); //Update time received for unsent messages

                        serverUI.addInfo(m.getTimeSent(),
                                m.getTimeReceived(),
                                ", Message from " + m.getSender().getUsername() +
                                        " to " + user.getUsername());

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
                        ClientThread ch = (ClientThread) mapElement.getValue();
                        ch.getOs().writeObject(uList);
                    }
                    connectedUsers = uList;
                }
            }

            /**
             * Sends the message to the receivers
             * @param m
             * @throws IOException
             */
            public void sendCurrMessage(Message m) throws IOException {
                for (User user : m.getReceivers()) {
                    if (user.getUsername().startsWith("Null")) {
                        unsendMessages.put(user, m);


                    } else {
                        for (User u : newClients.getClients().keySet()) {
                            if (Objects.equals(u.getUsername(), user.getUsername())) {

                                serverUI.addInfo(m.getTimeSent(),
                                        m.getTimeReceived(),
                                        ", Message from " + m.getSender().getUsername() +
                                                " to " + u.getUsername());

                                newClients.getClients().get(u).os.writeObject(m);
                            }
                        }
                        newClients.getClients().get(user).os.flush();
                    }
                }
            }
        }

        /**
         * Checks if two arrays are equal
         * @param array1
         * @param array2
         * @return
         */
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

    public static void main(String[] args) {
        Server server = new Server(1441);
        server.start();
    }
}