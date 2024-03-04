package io.codeforall.ironMaven;

public class Card {


    private char suit;
    private Rank rank;

    public Card(char suit, int rank) {
        this.suit = suit;
        this.rank = new Rank(rank);
    }

    public void setSuit(char suit) {
        this.suit = suit;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public char getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank.getName() + " " + suit;
    }
}
