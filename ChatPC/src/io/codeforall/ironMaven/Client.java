package io.codeforall.ironMaven;

import org.academiadecodigo.bootcamp.Prompt;

import java.io.*;
import java.net.Socket;

public class Client {

    private String name;
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;

    private String message;
    private Server server;

    private Card[] cards;
    private Prompt prompt;



    public Client(Server server, Socket clientSocket) {
        try {
            this.name = "User " + server.getPlayers().size();
            this.clientSocket = clientSocket;
            this.server = server;

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.prompt = new Prompt(clientSocket.getInputStream(),new PrintStream(out));


            System.out.println("new client");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Prompt getPrompt() {
        return prompt;
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
