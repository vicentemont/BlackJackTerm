package io.codeforall.ironMaven;

public class Deck {
    Card[] cardDeck = new Card[52];
    Suit[] suits = {Suit.DIAMONDS,Suit.CLUBS,Suit.HEARTS,Suit.SPADES};


    public Deck() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {

                Card card = cardDeck[i + (i + 1) * j];
                card = new Card(suits[i],j);
                System.out.println(card);

            }

        }
    }
}
