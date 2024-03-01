package io.codeforall.ironMaven;

import javax.naming.spi.Resolver;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket;
    private int port;
    private InetAddress inetAddress;
    private LinkedList<Client> clientLinkedList = new LinkedList<>();


    public Server() {

        port = 8080;
        try {
            inetAddress = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        while (true) {

            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established!");
                Client newClient = new Client(this, clientSocket);
                clientLinkedList.add(newClient);
                pool.submit(new Handler(newClient));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public int getPort() {
        return port;
    }

    public LinkedList<Client> getClientLinkedList() {
        return clientLinkedList;
    }

    private synchronized void broadcast(String message, Client clientThatBroadcasts) {
        for (Client client : this.getClientLinkedList()) {
            try {
                if (message != null) {
                    if (client == clientThatBroadcasts) {
                        client.getOut().write(("You("+ clientThatBroadcasts.getName() +"): ").getBytes());
                    } else {
                        client.getOut().write((clientThatBroadcasts.getName() + ": ").getBytes());
                    }
                    client.getOut().write(message.getBytes());
                    client.getOut().write('\n');
                    client.getOut().flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class Handler implements Runnable {
        private Client client;
        private String message;

        public Handler(Client client) throws IOException {
            this.client = client;
        }


        @Override
        public void run() {
            System.out.println("here");
            try {
                while ((message = client.getIn().readLine()) != null) {
                    client.setMessage(message);
                    broadcast(message, client);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }


}
