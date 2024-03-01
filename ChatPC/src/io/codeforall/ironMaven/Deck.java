package io.codeforall.ironMaven;

import java.util.Collections;
import java.util.LinkedList;

import java.util.LinkedList;

public class Deck {
    private LinkedList<Card> cardDeck;
    Suit[] suits = {Suit.DIAMONDS, Suit.CLUBS, Suit.HEARTS, Suit.SPADES};

    public Deck() {
        cardDeck = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                Card card = new Card(suits[i], j);
                cardDeck.add(card);
                System.out.println(card);
            }
        }


    }

    public void shuffle() {
        Collections.shuffle(cardDeck);
    }

    public Card dealCard() {
        if (!cardDeck.isEmpty()) {
            return cardDeck.removeFirst();
        } else {
            System.out.println("Deck is empty.");
            return null;
        }
    }

    public static void main(String[] args) {
        new Deck();
    }
}


