package io.codeforall.ironMaven;

public class Card {



    private Suit suit;
    private Rank rank;

    public Card(Suit suit, int rank){
        this.suit = suit;
        this.rank = new Rank(rank);
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString(){
        return rank.name + " of " + suit.toString();
    }
}
