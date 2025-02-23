package ui;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
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
            getBoardPrintOut(board);
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
            case "p":
                handlePlay(p);
                break;
            case "S":
            case "s":
                handleSwap(p);
                break;
            case "K":
            case "k":
                handleSkip(p);
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
                handlePositionAndDirectionSelection(player);
                break;
            } else {
                System.out.println("Invalid entry");
                break;
            }
        }
    }

    public void handlePositionAndDirectionSelection(Player player) {
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
            int score = board.playWord(player.getSelectedTiles(), row, col, dir);
            player.removeSelectedTiles();
            System.out.println(player.getPlayerName() + " earned " + score + " points!");
            player.addPoints(score);
            System.out.println(player.getPlayerName() + " now has " + player.getPointsThisGame() + " points \n");
        } else {
            System.out.println("Can't play that word there");
            player.clearSelectedTiles();
            handlePlay(player);
        }
    }

    public void handleSwap(Player player) {
        while (true) {
            System.out.println("Enter indices for tiles to swap, C to confirm, or any other character to cancel");
            if (scanner.hasNextInt()) {
                player.selectTile(scanner.nextInt());
                System.out.println("So far you've selected: ");
                printSelectedTiles(player);
            } else if (scanner.hasNext("C")) {
               // List<LetterTile> copyTiles = player.copySelectedTiles();
              //  Move move = new Move(player, board, copyTiles, )
              //Need to get NEW tiles
                player.swapTiles();
                System.out.println("Your new tiles are: ");
                getTilePrintOut(player);
                break;
            } else {
                player.clearSelectedTiles();
                break;
            }
        }
    }

    public void handleSkip(Player player) {
        player.clearSelectedTiles();
        //player.swapTiles();
        System.out.println(player.getPlayerName() + " skipped their turn \n");
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

    //!!!
    public boolean validateWord(List<LetterTile> letters, int row, int col, Direction dir) {
        return board.canPlay(letters, row,col, dir);
    }

    // EFFECTS: Prints a separator to the console
    public void printoutSpacer() {
        String spacer = "";
        for (int i = 0; i < 74; i++) {
            spacer += "-";
        }
        System.out.println(spacer);
    }

    // MODIFIES: this
    // EFFECTS: Ends the Scrabble Game
    public void endGame() {
        this.gameRunning = false;
    }

    public void getBoardPrintOut(Board board){
        String rowPrintOut;
        String header = "|";
        for (int i = 0; i <= 9; i++) {
            header += "_" + Integer.toString(i) + "_| ";
        }
        for (int i = 10; i < Board.BOARD_LENGTH; i++){
            header += Integer.toString(i) + "_| ";
        }
        System.out.println(header);
        printoutSpacer();
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            rowPrintOut = "";
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                Tile tile = board.getTileAtPositionOnBoard(i,j);
                if (tile instanceof BoardTile) {
                    BoardTile boardTile = (BoardTile) tile;
                    TileType type = boardTile.getTileType();
                    switch (type) {
                        case NORMAL:
                            rowPrintOut += "___| ";
                            break;
                        case DOUBLE_LETTER:
                            rowPrintOut += "DLS| ";     
                            break;                       
                        case DOUBLE_WORD:
                            rowPrintOut += "DWS| ";
                            break;
                        case TRIPLE_LETTER:
                            rowPrintOut += "TLS| ";
                            break;
                        case TRIPLE_WORD:
                            rowPrintOut += "TWS| ";
                            break;
                    }
                } else {
                    LetterTile letter = (LetterTile) tile;
                    rowPrintOut += "_" + getLetterString(letter) + "_; ";
                }
            }
            System.out.println(rowPrintOut + "|" + Integer.toString(i) +  "\n");
        }
    }
    
    // Play the game
    public static void main(String[] args) {
        //!!! 
        //Update later to prompt number of players and use scanner
        new ScrabbleApp();
    }
}
