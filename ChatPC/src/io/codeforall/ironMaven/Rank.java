package io.codeforall.ironMaven;

public class Rank {
    int value;
    String name;

    public Rank(int cardRankValue) {
        this.value = cardRankValue;
        if (cardRankValue >9) {
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
        } else if (cardRankValue == 0) {
            this.name = "Ace";
        } else {
            this.name = String.valueOf(cardRankValue + 1);
        }
    }
}

