package io.codeforall.ironMaven;

import org.academiadecodigo.bootcamp.Prompt;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Client {

    private String name;
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private PrintStream printStream;
    private Prompt prompt;
    private String message;
    private Server server;
    private int bet;
    private int betTotal;
    private int money = 100;

    private LinkedList<Card> hand = new LinkedList<>();


    public Client(Server server, Socket clientSocket) {
        try {
            this.name = "User " + server.getPlayers().size();
            this.clientSocket = clientSocket;
            this.server = server;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());
            printStream = new PrintStream(out);
            prompt = new Prompt(clientSocket.getInputStream(), printStream);

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

    public Prompt getPrompt() {
        return prompt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    public int calculateHandValue() {
        int totalValue = 0;
        int aceCount = 0;
        for (Card card : hand) {
            int cardValue = card.getRank().getValue();
            if (cardValue == 1) { // Ace
                aceCount++;
                cardValue = 11; // Assume ace as 11 initially
            }
            totalValue += cardValue;
        }
        // Adjust ace values if necessary
        while (totalValue > 21 && aceCount > 0) {
            totalValue -= 10; // Change ace value from 11 to 1
            aceCount--;
        }
        return totalValue;
    }
    public void hit(Dealer dealer) {
        Card card = dealer.dealCard();
        betTotal = betTotal +bet;
        if (card != null) {
            hand.add(card);
        } else {
            System.out.println("Deck is empty. Unable to deal card.");
        }
    }
    public void addToHand(Card card) {
        hand.add(card);
    }

    public LinkedList<Card> getHand() {
        return hand;
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
