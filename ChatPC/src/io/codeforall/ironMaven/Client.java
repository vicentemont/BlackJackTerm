package io.codeforall.ironMaven;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;

public class Client {
    private String name;
    private Socket clientSocket;
    private BufferedReader in;
    private DataOutputStream out;
    private Server server;
    private LinkedList<Card> hand;
    private int bet;

    private int betTotal;
    private int money = 100; // Starting money

    public Client(Server server, Socket clientSocket) {
        try {
            this.name = "User " + server.getClientLinkedList().size();
            this.clientSocket = clientSocket;
            this.server = server;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.hand = new LinkedList<>();
            this.bet = 0;

            System.out.println("New client");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stand() {
        // Client chooses to stand, no action needed here
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
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

    public int getMoney() {
        return money;
    }

}
