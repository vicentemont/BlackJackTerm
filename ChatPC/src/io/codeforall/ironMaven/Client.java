package io.codeforall.ironMaven;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private String name;
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;

    private String message;
    private Server server;

    private Card[]



    public Client(Server server, Socket clientSocket) {
        try {
            this.name = "User " + server.getClientLinkedList().size();
            this.clientSocket = clientSocket;
            this.server = server;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());


            System.out.println("new client");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BufferedReader getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
