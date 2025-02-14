package ui;

import model.Board;
import model.TileBag;
import model.Player;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

// Represents a game of Scrabble

public class ScrabbleApp {
    private Player player;
    private Board board;
    private TileBag tileBag;
    private boolean gameRunning;
    private Scanner scanner;
    private List<Player> players;
    private int numPlayers; 

    //EFFECTS: Creates new ScrabbleApp with
    //        a board and tile bag
    public ScrabbleApp() {        
        printoutSpacer();
        System.out.println("Welcome to Scrabble in Java");
        printoutSpacer();
        initializeGame();

    }
    
    public void initializeGame() {
        board = new Board();
        tileBag = new TileBag();
        scanner = new Scanner(System.in);
        this.gameRunning = true;
        initializePlayers();

        while (gameRunning) {
            handleGameplay();
        }
    }
    public void initializePlayers() {
        System.out.println("Please enter the number of players [1,4]");
        int numPlayers = this.scanner.nextInt();
        String numPlayerConfirmation = Integer.toString(numPlayers);
        System.out.println("You entered " + numPlayerConfirmation + " players. Is that correct? Press (Y) to confirm or (N) to cancel and re-enter");
        scanner.nextLine();
        String confirmOrCancel = this.scanner.nextLine();
        printoutSpacer();
        if (confirmOrCancel.equals("Y")) {
            this.numPlayers = numPlayers;
            requestPlayerNames(numPlayers);
        } else if (confirmOrCancel.equals("N")) {
            initializePlayers();
        } else {
            System.out.println("Invalid entry. Press (Y) to confirm or (N) to cancel and re-enter");
        }
    }
    // REQUIRES: First move of the game hasn't been played.
    // MODIFIES: this
    // EFFECTS: Prompts player to enter players' names
    //         in the desired order of play.
    public void requestPlayerNames(int numPlayers) {
        players = new ArrayList<>();
        System.out.println("Please enter each player's name, wrapped in \"\" separately, in the order you want to play");
        while (players.size() < numPlayers) {
            String inputPlayerName = scanner.nextLine();
            System.out.println("You entered " + inputPlayerName + ". Press (Y) to confirm or (N) to cancel and re-enter");
            String confirmName = scanner.nextLine();
            if (confirmName.equals("Y")) {
                player = new Player(inputPlayerName, board, tileBag);
                players.add(player);
            } else if (confirmName.equals("N")) {
                System.out.println("Okay, name wasn't saved. Please re-enter the correct name");
            } else {
                System.out.println("Invalid confirmation response. Please re-enter the desired name");
            }
            printoutSpacer();
        }
    }

    public void handleGameplay() {
        for (int i = 0; i < numPlayers; i++) {
            Player playerToPlayNext = players.get(i);
            playerToPlayNext.playTurn();
        }
    }

    // EFFECTS: Prints a separator to the console
    public void printoutSpacer() {
        System.out.println("---------------------------------------");
    }

    // MODIFIES: this
    // EFFECTS: Ends the Scrabble Game
    public void endGame() {
        this.gameRunning = false;
    }

    // Play the game
    public static void main(String[] args) {
        //!!! 
        //Update later to prompt number of players and use scanner
        new ScrabbleApp();
    }
}
