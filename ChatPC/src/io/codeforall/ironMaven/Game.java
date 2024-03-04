package io.codeforall.ironMaven;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;


public class Game {
    private ServerSocket serverSocket;
    private int port;
    private InetAddress inetAddress;
    private LinkedList<Player> players = new LinkedList<>();
    private LinkedList<Player> playersInGame = new LinkedList<>();

    private Dealer gameDealer;
    private Deck gameDeck;
    private boolean round = true;
    private int numberOfCurrentPlayers;
    private int playersInLobby;
    private int maxPlayers = 5;
    private boolean isGamePlaying;


    public Game() {
        gameDeck = new Deck();
        gameDealer = new Dealer(this);
        port = 8080;
        try {
            inetAddress = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.startServer();
    }

    public int getPlayersInLobby() {
        return playersInLobby;
    }

    public Deck getGameDeck() {
        return gameDeck;
    }

    public void startServer() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try {
            pool.submit(new Handler(gameDealer));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {

            try {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Connection established!");
                playersInLobby++;
                Player newPlayer = new Player(this, clientSocket);
                players.add(newPlayer);
                round = true;

                pool.submit(new Handler(newPlayer));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void roundOver() {
        for (int i = 0; i < gameDealer.getHand().size(); i++) {
            gameDealer.setHand(new LinkedList<>());
        }
        for (Player player : playersInGame) {
            for (int i = 0; i < player.getHand().size(); i++) {
                player.totalValue=0;
                player.setHand(new LinkedList<>());
            }
        }
        gameDeck = new Deck();
        broadcast(String.valueOf(gameDeck.getCardDeck().size()),gameDealer);

    }

    public void startMenu(Player player) {

        String[] menuOptions = {"Enter Game", "Watch Table", "Go to Bank"};

        try {
            player.getOut().write("<<<<<<<< Welcome to XIVIC's BlhackJack game >>>>>>>>>\n\n".getBytes());
            player.getOut().write(("Currently playing: " + numberOfCurrentPlayers + " players\n").getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MenuInputScanner menuInputScanner = new MenuInputScanner(menuOptions);
        menuInputScanner.setMessage("Choose one of the options below:");
        int answerIndex = player.getPrompt().getUserInput(menuInputScanner) - 1;

        switch (answerIndex) {
            case 0:
                playersInGame.add(player);
                numberOfCurrentPlayers++;
                broadcast(player.getName() + " joined the game ", player);
                break;
            case 1:
                try {
                    player.getOut().write("Alright 2".getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case 2:
                try {
                    player.getOut().write("Alright 3".getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                try {
                    player.getOut().write("Alright def".getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    public void showTable() {
        broadcastHand(gameDealer);
        for (Player p : playersInGame) {
            broadcastHand(p);
        }
    }

    public void hit(Player player) {
        try {
            player.getOut().write("You got a new card\n".getBytes());
            broadcast((player.getName() + " got an " + player.hit(gameDealer)), gameDealer);
            int handTotal = player.calculateHandValue();
            if (player.isBust()) {
                broadcast((player.getName() + " total is " + handTotal + " BUSTED!!!"), gameDealer);
            }
            if (handTotal <= 21) {
                player.getOut().write(("You have " + handTotal + " points").getBytes());
                hitPlayMenu(player);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stand(Player player) {
        int handTotal = player.calculateHandValue();
        showTable();
        broadcast("Stand", player);


    }

    public void playMenu(Player player) {
        String[] menuOptions = {"Hit", "Double", "Stand"};
        try {
            player.getOut().write("Your turn!".getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MenuInputScanner menuInputScanner = new MenuInputScanner(menuOptions);
        menuInputScanner.setMessage("Choose one of the options below:");
        int answerIndex = player.getPrompt().getUserInput(menuInputScanner) - 1;

        switch (answerIndex) {
            case 0:
                hit(player);
                break;
            case 1:
                stand(player);
                break;
            case 2:
                stand(player);
                break;
            default:
                try {
                    player.getOut().write("Option not available!".getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    public void hitPlayMenu(Player player) {
        showTable();

        String[] menuOptions = {"Hit", "Stand"};
        MenuInputScanner menuInputScanner = new MenuInputScanner(menuOptions);
        menuInputScanner.setMessage("Choose one of the options below:");
        int answerIndex = player.getPrompt().getUserInput(menuInputScanner) - 1;

        switch (answerIndex) {
            case 0:
                hit(player);
                break;
            case 1:
                stand(player);
                break;
            default:
                try {
                    player.getOut().write("Option not available!".getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    public int getPort() {
        return port;
    }

    public LinkedList<Player> getPlayersInGame() {
        return playersInGame;
    }

    private synchronized void broadcast(String message, GamePlayer playerThatBroadcasts) {
        for (Player player : this.getPlayersInGame()) {
            try {
                if (message != null) {
                    if (player == playerThatBroadcasts) {
                        player.getOut().write(("You(" + playerThatBroadcasts.getName() + "): ").getBytes());
                    } else {
                        player.getOut().write((playerThatBroadcasts.getName() + ": ").getBytes());
                    }
                    player.getOut().write(message.getBytes());
                    player.getOut().write('\n');
                    player.getOut().flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastHand(GamePlayer gamePlayer) {
        String str = "";
        for (Card card : gamePlayer.getHand()) {
            str = str.concat("| " + card.toString() + " |");
        }
        broadcast((gamePlayer.getName() + " hand is: " + str), gameDealer);

    }


    public void startGame() {


        while (playersInGame.size() > 0 && round) {
            isGamePlaying = true;
            roundOver();
            delay(2000);
            broadcast("<<<<<<<< New Round >>>>>>>>\nShuffling Cards...\n", gameDealer);
            delay(2000);
            gameDealer.dealInitialCards();
            broadcastHand(gameDealer);
            delay(2000);
            for (Player player : playersInGame) {
                broadcastHand(player);
                delay(2000);
            }
            for (Player player : playersInGame) {
                playMenu(player);
            }
            dealerPlay();
            for (Player player : playersInGame) {
                try {
                    player.getOut().write(gameDealer.compareWithClient(player).getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }

    public void dealerPlay() {
        while (gameDealer.calculateHandValue() < 17) {
            gameDealer.hit(gameDealer);
            broadcastHand(gameDealer);
            delay(2000);
        }
        if (gameDealer.calculateHandValue() > 21) {
            broadcast(("Dealer busted"), gameDealer);
        }
    }

    public void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public class Handler implements Runnable {
        private GamePlayer player;
        private String message;

        public Handler(GamePlayer player) throws IOException {
            this.player = player;

        }


        @Override
        public void run() {
            System.out.println("here");
            if (player.getClass() == Player.class) {
                startMenu((Player) player);
                if (isGamePlaying) {
                    try {
                        ((Player) player).getOut().write("A game is currently playing...".getBytes());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            } else {
                while (true && round) {
                    startGame();
                    isGamePlaying = false;
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }


}
