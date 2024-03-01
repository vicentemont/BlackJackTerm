package io.codeforall.ironMaven;

import java.util.LinkedList;

import java.util.LinkedList;

import java.util.LinkedList;

public class Dealer {
    private LinkedList<Card> hand;
    private Deck deck;
    private LinkedList<Client> clients;

    public Dealer(Deck deck) {
        this.deck = deck;
        this.hand = new LinkedList<>();
        this.clients = new LinkedList<>();
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void dealInitialCards() {
        for (Client client : clients) {
            client.addToHand(deck.dealCard());
        }
        addToHand(deck.dealCard());
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public LinkedList<Card> getHand() {
        return hand;
    }

    public Card dealCard() {
        return deck.dealCard();
    }


    private int calculateHandValue() {
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


    public String compareWithClient(Client client) {
        int dealerValue = calculateHandValue();
        int clientValue = client.calculateHandValue();

        if (dealerValue > 21) {
            return "Dealer busts. Client wins.";
        } else if (clientValue > 21 || dealerValue > clientValue) {
            return "Dealer wins.";
        } else if (dealerValue < clientValue) {
            return "Client wins.";
        } else {
            return "It's a tie.";
        }
    }
}
