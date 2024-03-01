package io.codeforall.ironMaven;

import java.util.LinkedList;

public abstract class GamePlayer {
    private String name;
    int totalValue = 0;
    private LinkedList<Card> hand = new LinkedList<>();

    private boolean bust;

    public int calculateHandValue() {

        int aceCount = 0;
        int sum = 0;
        for (Card card : hand) {
            int cardValue = card.getRank().getValue();
            if (cardValue == 1) { // Ace
                aceCount++;
                cardValue = 11; // Assume ace as 11 initially
            }
            sum += cardValue;
        }
        // Adjust ace values if necessary
        while (sum > 21 && aceCount > 0) {
            sum -= 10; // Change ace value from 11 to 1
            aceCount--;
        }

        if (sum > 21) {
            this.bust = true;
        }
        return sum;
    }

    public boolean isBust() {
        return bust;
    }

    public void setBust(boolean bust) {
        this.bust = bust;
    }

    public Card hit(Dealer dealer) {
        Card card = dealer.dealCard();
        if (card != null) {
            hand.add(card);
            return card;
        } else {
            System.out.println("Deck is empty. Unable to deal card.");
            return null;
        }

    }


    public LinkedList<Card> getHand() {
        return hand;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void add(Card card) {
        this.hand.add(card);
    }
}
