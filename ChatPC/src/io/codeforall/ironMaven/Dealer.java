package io.codeforall.ironMaven;

public class Dealer extends GamePlayer {
    private Deck deck;
    private Game game;

    public Dealer(Game game) {
        super();
        this.setName("Dealer");
        this.game = game;
        this.deck = game.getGameDeck();

    }


    public void dealInitialCards() {
        for (Player player : game.getPlayersInGame()) {
            giveCard(player);
            giveCard(player);
        }
        giveCard(this);
        giveCard(this);
    }
    public void giveCard(GamePlayer player) {
        player.add(dealCard());
    }
    public Card dealCard() {
        return deck.dealCard();
    }



    public String compareWithClient(Player player) {
        int dealerValue = calculateHandValue();
        int clientValue = player.calculateHandValue();

        if (dealerValue > 21) {
            return "Dealer busted, you win!";
        } else if (clientValue > 21 || dealerValue > clientValue) {
            return "Dealer win.";
        } else if (dealerValue < clientValue) {
            return "Client wins.";
        } else {
            return "It's a tie.";
        }
    }
}
