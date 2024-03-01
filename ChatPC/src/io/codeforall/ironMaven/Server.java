package io.codeforall.ironMaven;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.PasswordInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringSetInputScanner;


public class Server {
    private ServerSocket serverSocket;
    private int port;
    private InetAddress inetAddress;
    private LinkedList<Client> players = new LinkedList<>();
    private Dealer gameDealer;
    private Deck gameDeck;
    private boolean round;
    private int numberOfCurrentPlayers;
    private int maxPlayers = 5;

    private String[] menuOptions = {"Enter Game", "Watch Table" , "Go to Bank"};




    public Server() {
        gameDeck = new Deck();
        gameDealer = new Dealer(gameDeck);
        port = 8080;
        try {
            inetAddress = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        while (true) {

            try {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Connection established!");
                Client newClient = new Client(this, clientSocket);
                players.add(newClient);
                startMenu(newClient);
                pool.submit(new Handler(newClient));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void startMenu(Client player) {

        try {
            player.getOut().write("<<<<<<<< Welcome to XIVIC's BlhackJack game >>>>>>>>>\n\n".getBytes());
            player.getOut().write(("Currently playing: " + numberOfCurrentPlayers + " players\n").getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MenuInputScanner menuInputScanner = new MenuInputScanner(this.menuOptions);
        menuInputScanner.setMessage("Choose one of the options below:");
        int answerIndex = player.getPrompt().getUserInput(menuInputScanner) - 1;

        switch (answerIndex) {
            case 0:
                try {
                    player.getOut().write("Alright 1".getBytes());
                    numberOfCurrentPlayers ++;
                    broadcast(player.getName() + "joined the game",player);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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


    public int getPort() {
        return port;
    }

    public LinkedList<Client> getPlayers() {
        return players;
    }

    private synchronized void broadcast(String message, Client clientThatBroadcasts) {
        for (Client client : this.getPlayers()) {
            try {
                if (message != null) {
                    if (client == clientThatBroadcasts) {
                        client.getOut().write(("You("+ clientThatBroadcasts.getName() +"): ").getBytes());
                    } else {
                        client.getOut().write((clientThatBroadcasts.getName() + ": ").getBytes());
                    }
                    client.getOut().write(message.getBytes());
                    client.getOut().write('\n');
                    client.getOut().flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class Handler implements Runnable {
        private Client client;
        private String message;

        public Handler(Client client) throws IOException {
            this.client = client;
        }


        @Override
        public void run() {
            System.out.println("here");
            try {
                while ((message = client.getIn().readLine()) != null) {
                    client.setMessage(message);
                    broadcast(message, client);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }


}
