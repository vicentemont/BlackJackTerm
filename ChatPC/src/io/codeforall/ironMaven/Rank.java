package io.codeforall.ironMaven;

public class Rank {
    private int value;
    private String name;

    public String getName() {
        return name;
    }

    public Rank(int cardRankValue) {
        this.value = cardRankValue;
        if (cardRankValue >= 10 && cardRankValue <= 12) {
            switch (cardRankValue) {
                case 10:
                    this.name = "Jack";
                    break;
                case 11:
                    this.name = "Queen";
                    break;
                case 12:
                    this.name = "King";
                    break;
            }
            this.value = 10; // Value for Jack, Queen, King is 10
        } else if (cardRankValue == 0) {
            this.name = "Ace";
            this.value = 1; // Default value for Ace is 1
        } else {
            this.name = String.valueOf(cardRankValue + 1);
            this.value = cardRankValue + 1;
        }
    }

    public int getValue() {
        return value;
    }
}
