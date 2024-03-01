package io.codeforall.ironMaven;

import org.academiadecodigo.bootcamp.Prompt;

import java.io.*;
import java.net.Socket;

public class Player extends GamePlayer {

    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private PrintStream printStream;
    private Prompt prompt;
    private String message;
    private Game game;
    private int bet;
    private int betTotal;
    private int money = 100;

    public Player(Game game, Socket clientSocket) {
        try {
            this.setName("User " + game.getPlayersInGame().size());
            this.clientSocket = clientSocket;
            this.game = game;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());
            printStream = new PrintStream(out);
            prompt = new Prompt(clientSocket.getInputStream(), printStream);

            System.out.println("new client");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public Prompt getPrompt() {
        return prompt;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void doubleBet(Dealer dealer) {
        if (bet * 2 <= money) {
            bet *= 2;
            money -= bet;
            hit(dealer);
            betTotal = betTotal +2*bet;
        } else {
            System.out.println("Insufficient funds to double bet.");
        }
    }
}
