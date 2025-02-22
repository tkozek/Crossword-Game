package ui;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.nio.channels.Pipe.SourceChannel;
import java.util.ArrayList;
import model.*;

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
        System.out.println("You entered " 
                + numPlayerConfirmation 
                + " players. Is that correct? Press (Y) to confirm or (N) to cancel and re-enter");
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
        System.out.println(
                "Please enter each player's name separately, in the order you want to play");
        while (players.size() < numPlayers) {
            String inputPlayerName = scanner.nextLine();
            System.out.println("You entered " 
                    + inputPlayerName + ". Press (Y) to confirm or (N) to cancel and re-enter");
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
            tileBag.drawTiles(playerToPlayNext);
            System.out.println("Here are your tiles: " + playerToPlayNext.getPlayerName());
            getTilePrintOut(playerToPlayNext);
            handleTurn(playerToPlayNext);
        }
    }

    public void handleTurn(Player p) {
        System.out.println("Type whether you'd like to (P)lay, (S)wap, S(k)ip");
        
        switch (scanner.nextLine()) {
            case "P":
                handlePlay(p);
                break;
            case "S":
                handleSwap();
                break;
            case "K":
                handleSkip();
                break;
        }
    }

    public void handlePlay(Player player) {
        while (true) {
            System.out.println("Enter the index of the tiles you'd like to play, in the order they form your word "
                    + "or enter C to confirm");
            if (scanner.hasNextInt()) {
                player.selectTile(scanner.nextInt());
                System.out.println("So far you've selected: ");
                printSelectedTiles(player);
            } else if (scanner.hasNext("C")) {
                scanner.nextLine();
                System.out.println("Enter the row index you'd like to start your word at \n ");
                scanner.nextLine();
                int row = scanner.nextInt();

                System.out.println("Enter the column index you'd like to start your word at");
                int col = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Now enter direction (R)ight or (D)own (default)");
                Direction dir = (scanner.nextLine().equals("R")) ? Direction.RIGHT : Direction.DOWN;
                if (board.canPlay(player.getSelectedTiles(), row, col, dir)) {
                    board.playWord(player.getSelectedTiles(), row, col, dir);
                    player.removeSelectedTiles();
                    break;
                } else {
                    System.out.println("Can't play that word there");
                }
            }
        }
    }


    public void handleSwap() {
        while (true) {
            System.out.println("Enter the index of the tiles you'd like to swap or enter \"C\" to confirm");
            if (scanner.hasNextInt()) {
                player.selectTile(scanner.nextInt());
                System.out.println("So far you've selected: ");
                printSelectedTiles(player);
            } else if (scanner.hasNext("C")) {
                player.swapTiles();
                System.out.println("Your new tiles are: ");
                getTilePrintOut(player);
                break;
            } else {
                System.out.println("Invalid entry, try again");
            }
        }
    }

    public void handleSkip() {
        player.clearSelectedTiles();
        player.swapTiles();
        System.out.println("You've skipped your turn");
    }

    //EFFECTS: Prints out selected tiles
    // for player
    public void printSelectedTiles(Player p) {
        String tilePrintOut = "";
        List<LetterTile> playersSelectedLetters = p.getSelectedTiles();
        for (LetterTile letter : playersSelectedLetters) {
            tilePrintOut += getLetterString(letter);
        }
        System.out.println(tilePrintOut + "\n");
    }

    //EFFECTS: Prints out all tiles on the
    // player's tile rack
    public void getTilePrintOut(Player p) {
        String tilePrintOut = "";
        List<LetterTile> playersLetters = p.getTilesOnRack();
        for (LetterTile letter : playersLetters) {
            tilePrintOut += getLetterString(letter);
        }
        System.out.println(tilePrintOut + "\n");
    }

    public String getLetterString(LetterTile letter) {
        char character = letter.getCharacter();
        return String.valueOf(character);
    }

    // EFFECTS: Prints remaining character counts
    // for tiles not on the board or the player's tile rack
    public void getRemainingCharacterCounts(Player p) {
        Map<Character, Integer> remainingCounts = p.getNumEachCharInBagAndOpponents();
        for (Map.Entry<Character, Integer> entry : remainingCounts.entrySet()) {
            System.out.println(entry.getKey() + "" + entry.getValue());
        }
    }


    //EFFECTS: returns a string combining
    // all player's tiles in their rack
    // in order
    public String getTilesOnRack(Player p) {
        List<LetterTile> letters = p.getTilesOnRack();
        String toDisplay = "";
        for (LetterTile letter : letters) {
            toDisplay += letter.getStringToDisplay();
        }
        return toDisplay;
    } 


    public boolean validateWord(List<LetterTile> letters, int row, int col, Direction dir) {
        return board.canPlay(letters, row,col, dir);
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
