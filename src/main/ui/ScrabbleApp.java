package ui;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import model.*;
import model.board.Board;
import model.board.BoardTile;
import model.move.Move;
import model.move.MoveType;
import model.tile.LetterTile;
import model.tile.Tile;
import model.tile.TileBag;
import model.tile.TileType;
import persistance.JsonReader;
import persistance.JsonWriter;

// Citation: Saving and loading are based on JSON example from edX
// Represents a game of Scrabble
public class ScrabbleApp {
    private static final String JSON_STORE = "./data/gameToPlayTest.json";
    private Player player;
    private Board board;
    private TileBag tileBag;
    private boolean gameRunning;
    private Scanner scanner;
    private List<Player> players;
    private int numPlayers; 
    private ScrabbleGame scrabbleGame;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    
    //EFFECTS: Creates new ScrabbleApp with
    //        a board and tile bag
    public ScrabbleApp() {        
        printoutSpacer();
        System.out.println("Welcome to Scrabble in Java");
        printoutSpacer();
        scanner = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        tileBag = new TileBag();
        System.out.println("(L)oad your old game or (p)lay a new one?");
        switch (scanner.nextLine()) {
            case "P":
            case "p":
                initializeNewGame();
                break;
            case "L":
            case "l":
                loadOldGame();
                break;
            default:
                initializeNewGame();
        }
        this.gameRunning = true;
        handleGameplay();
    }
    
    // MODIFIES: player, board, tileBag
    // EFFECTS: creates assets for a new game and prompts user input
    // for setup parameters
    public void initializeNewGame() {
        board = new Board();
        tileBag = new TileBag();
        scrabbleGame = new ScrabbleGame("", board, tileBag);
        this.gameRunning = true;
        initializePlayers();
    }

    // MODIFIES: scrabbleGame, players, tileBag, board, numPlayers
    // EFFECTS: loads assets from previously saved game
    private void loadOldGame() {
        this.gameRunning = true;
        try {
            scrabbleGame = jsonReader.read();
            this.players = scrabbleGame.getPlayers();
            this.board = scrabbleGame.getBoard();
            this.tileBag = scrabbleGame.getTileBag();
            this.numPlayers = players.size();
            System.out.println("Loaded " + scrabbleGame.getName() + " with " + String.valueOf(numPlayers) 
                    + " players from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read game from file: " + JSON_STORE);
        }
    }

    // MODIFIES: players
    // EFFECTS: Prompts input for number of player's
    // and requests their names
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
                player = new Player(inputPlayerName, board, tileBag, scrabbleGame);
                players.add(player);
                scrabbleGame.addPlayer(player);
            } else if (confirmName.equals("N")) {
                System.out.println("Okay, name wasn't saved. Please re-enter the correct name");
            } else {
                System.out.println("Invalid confirmation response. Please re-enter the desired name");
            }
            printoutSpacer();
        }
    }

    // MODIFIES: player, board, tileBag
    // EFFECTS: Manages order of turn taking, 
    // and ensures player's draw new tiles
    // when they are supposed to
    public void handleGameplay() {
        int index;
        while (gameRunning) {
            for (int i = 0; i < numPlayers; i++) {
                index = (i + scrabbleGame.getFirstPlayerIndex()) % numPlayers;
                getBoardPrintOut(board);
                Player playerToPlayNext = players.get(index);
                tileBag.drawTiles(playerToPlayNext);
                handleTurn(playerToPlayNext);
            }
        }
    }

    // MODIFIES: player, board, tileBag
    // EFFECTS: prompts player to decide which 
    // type their next move will be, or if they
    // want to view something
    public void handleTurn(Player p) {
        System.out.println("\n Here are your tiles: " + p.getPlayerName());
        getTilePrintOut(p);
        System.out.println("Type whether you'd like to (P)lay, (S)wap, S(k)ip, or (o)ther.");
        switch (scanner.nextLine().toLowerCase()) {
            case "p":
                handlePlay(p);
                scanner.nextLine();
                break;
            case "s":
                handleSwap(p);
                scanner.nextLine();
                break;
            case "k":
                handleSkip(p);
                scanner.nextLine();
                break;
            case "o":
                handleNonPlayOptions(p);
                break;
            default:
                handleTurn(p);
        }
    }

    // MODIFIES: this
    // EFFECTS: Let's player decide to view
    // game related info, or save and quit
    public void handleNonPlayOptions(Player p) {
        System.out.println("\nType whether you'd like to (v)iew something, s(a)ve and quit, "
                + "or (q)uit without saving");
        switch (scanner.nextLine()) {
            case "V":
            case "v":
                handleViewings(p);
                break;
            case "A":
            case "a":
                handleSave(p);
                this.gameRunning = false;
                System.exit(0);
            case "Q":
            case "q":
            // !!! Consider adding confirmation
                this.gameRunning = false;
                System.exit(0);
            default:
                handleNonPlayOptions(p);
        }
    }

    // EFFECTS: Prompts user for what they
    // would like to view about the game,
    // displays that.
    public void handleViewings(Player p) {
        System.out.println("\n Type whether you'd like to view current (b)oard, (W)ords played, all (m)oves,"
                + " (r)emaining tile counts, or anything else to cancel");
        switch (scanner.nextLine()) {
            case "B":
            case "b":
                getBoardPrintOut(board);
                break;
            case "W":
            case "w":
                printWordsPlayed(p);
                break;
            case "M":
            case "m":
                printAllMovesSummary(p);
                break;
            case "R":
            case "r":
                getRemainingCharacterCounts(p);
                break;
        }
        handleTurn(p);
    }

    // MODIFIES: scrabbleGame
    // EFFECTS: Saves game to file
    private void handleSave(Player player) {
        try {
            scrabbleGame.setFirstPlayer(player);
            jsonWriter.open();
            jsonWriter.write(scrabbleGame);
            jsonWriter.close();
            System.out.println("Saved" + scrabbleGame.getName() + " with " + String.valueOf(numPlayers)
                    + " players to " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to write to file " + JSON_STORE);
        }
    }

    // EFFECTS: Prints all player moves
    // in order, including playing words,
    // swaps, skips
    public void printAllMovesSummary(Player p) {
        List<Move> allMoves = p.getHistory().getMoves();
        MoveType moveType;
        for (Move move : allMoves) {
            moveType = move.getMoveType();
            if (moveType == MoveType.PLAY_WORD) {
                getWordPrintout(move, p);
            } else if (moveType == MoveType.SWAP_TILES) {
                printSwapSummary(move, p);
            } else if (moveType == MoveType.SKIP) {
                printSkipSummary(move, p);
            }
        }
    }

    //EFFECTS: Prints summary of a player swap
    public void printSwapSummary(Move swap, Player p) {
        String printout = "\n" + p.getPlayerName() + " swapped tiles. ";
        String preAndPostLetters = getWordString(swap.getLettersInvolved());
        int halfLength = preAndPostLetters.length() / 2;
        String preSwapLetters = preAndPostLetters.substring(0, halfLength);
        String postSwapLetters = preAndPostLetters.substring(halfLength);
        String points = String.valueOf(swap.getPointsForMove());
        printout += "Their tiles before swapping were: " + preSwapLetters + " and their tiles after swapping were " 
                 + postSwapLetters + ", earning " + points + " points.";
        System.out.println(printout);
    }

    // EFFECTS: Prints summary of a skipped turn
    public void printSkipSummary(Move skip, Player p) {
        System.out.println("\n" + p.getPlayerName() + " skipped their turn");
    }

    // EFFECTS: prints summary of all
    // words played by the player.
    public void printWordsPlayed(Player p) {
        List<Move> wordsPlayed = p.getHistory().getMovesWithWordPlayed();
        for (Move word : wordsPlayed) {
            getWordPrintout(word, p);
        }
    }

    // EFFECTS: merges letters' characters into one
    // string in their original order
    public String getWordString(List<LetterTile> letters) {
        String toDisplay = "";
        for (LetterTile letter : letters) {
            toDisplay += getLetterString(letter);
        }
        return toDisplay;
    }

    // EFFECTS: prints summary of a word played
    public void getWordPrintout(Move word, Player p) {
        String printout = "\n" + p.getPlayerName() + " played ";
        String wordString = getWordString(word.getLettersInvolved());
        String startRow = String.valueOf(word.getStartRow());
        String startCol = String.valueOf(word.getStartColumn());
        String coordinates = "(" + startRow + "," + startCol + ")";
        String direction = (word.getDirection() == Direction.RIGHT) ? "to the right" : "down";
        String points = String.valueOf(word.getPointsForMove());
        printout += wordString + " starting at " + coordinates + " and moving " 
                + direction + " earning " + points + " points.";
        System.out.println(printout);
    }

    // MODIFIES: player, board, tileBag
    // EFFECTS: Prompts player to input directions
    // to place a word on the board
    public void handlePlay(Player player) {
        while (true) {
            System.out.println("Enter the index of the tiles you'd like to play, in the order they form your word "
                    + "or enter C to confirm");
            if (scanner.hasNextInt()) {
                player.selectTile(scanner.nextInt() - 1);
                System.out.println("So far you've selected: ");
                printSelectedTiles(player);
            } else if (scanner.hasNext("C")) {
                handlePositionAndDirectionSelection(player);
                break;
            } else {
                System.out.println("Invalid entry");
                handleTurn(player);
            }
        }
    }

    // MODIFIES: player, board, tileBag
    // EFFECTS: Prompts player for position and direction 
    // instructions, then places and logs the turn
    // their selected tiles as directed 
    // OR 
    // prints error message if instructions
    // were invalid
    public void handlePositionAndDirectionSelection(Player player) {
        adjustScanner();
        System.out.println("Enter the row index you'd like to start your word at \n ");
        int row = scanner.nextInt();
        System.out.println("Enter the column index you'd like to start your word at");
        int col = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Now enter direction (R)ight or (D)own (default)");
        Direction dir = (scanner.nextLine().equals("R")) ? Direction.RIGHT : Direction.DOWN;
        if (board.canPlay(player.getSelectedTiles(), row, col, dir)) {
            int score = board.playWord(player.getSelectedTiles(), row, col, dir);
            player.logWord(row, col, score, dir);
            player.removeSelectedTiles();
            tileBag.drawTiles(player);
            System.out.println("\nYour new tiles are:");
            getTilePrintOut(player);
            System.out.println(player.getPlayerName() + " earned " + score + " points!");
            player.addPoints(score);
            System.out.println(player.getPlayerName() + " now has " + player.getPointsThisGame() + " points \n");
        } else {
            System.out.println("Can't play that word there");
            player.clearSelectedTiles();
            handlePlay(player);
        }
    }

    // MODIFIES: player, tileBag
    // EFFECTS: Prompts player to 
    // select tiles to swap, swaps and displays 
    // new tiles on confirmation, logs the swap
    public void handleSwap(Player player) {
        while (true) {
            System.out.println("Enter indices for tiles to swap, C to confirm, or any other character to cancel");
            if (scanner.hasNextInt()) {
                player.selectTile(scanner.nextInt() - 1);
                System.out.println("So far you've selected: ");
                printSelectedTiles(player);
            } else if (scanner.hasNext("C")) {
                List<LetterTile> preSwapLetters = player.copyLetterTiles(player.getTilesOnRack());
                player.swapTiles();
                List<LetterTile> postSwapLetters = player.copyLetterTiles(player.getTilesOnRack());
                player.logSwap(preSwapLetters, postSwapLetters);
                System.out.println("\n Your new tiles are: ");
                getTilePrintOut(player);
                adjustScanner();
                break;
            } else {
                adjustScanner();
                player.clearSelectedTiles();
                handleTurn(player);
                return;
            }
        }
    }

    // EFFECTS: Helper method to 
    // reduce method lengths where scanner
    // must be adjusted numerous times
    private void adjustScanner() {
        scanner.nextLine();
        scanner.nextLine();
    }

    // MODIFIES: player
    // EFFECTS: deselects players tiles
    // logs the skipped turn and prints confirmation
    public void handleSkip(Player player) {
        player.clearSelectedTiles();
        player.logSkippedTurn();
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

    
    // EFFECTS: returns string representing
    // a letter's character such as "A".
    public String getLetterString(LetterTile letter) {
        char character = letter.getCharacter();
        return String.valueOf(character);
    }

    // EFFECTS: Prints remaining character counts
    // for tiles not on the board or the player's tile rack
    public void getRemainingCharacterCounts(Player p) {
        printoutSpacer();
        System.out.println("The remaining tile counts in the format 'Letter : Count' are:");
        Map<Character, Integer> remainingCounts = scrabbleGame.getNumEachCharInBagAndOpponents(p);
        for (Map.Entry<Character, Integer> entry : remainingCounts.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        printoutSpacer();
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

    // EFFECTS: prints out the current board
    public void getBoardPrintOut(Board board) {
        printHeader();
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            String rowPrintOut = "";
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                Tile tile = board.getTileAtPositionOnBoard(i,j);
                if (tile instanceof BoardTile) {
                    BoardTile boardTile = (BoardTile) tile;
                    rowPrintOut += getBoardTileSymbol(boardTile);
                    
                } else {
                    LetterTile letter = (LetterTile) tile;
                    rowPrintOut += "_" + getLetterString(letter) + "_| ";
                }
            }
            System.out.println(rowPrintOut + "|" + Integer.toString(i) +  "\n");
        }
    }

    // EFFECTS: returns a string representing
    // the type of given board tile.
    public String getBoardTileSymbol(BoardTile boardTile) {
        TileType type = boardTile.getTileType();
        switch (type) {
            case NORMAL:
                return "___| ";
            case DOUBLE_LETTER:
                return "DLS| ";                   
            case DOUBLE_WORD:
                return "DWS| ";
            case TRIPLE_LETTER:
                return "TLS| ";
            case TRIPLE_WORD:
                return "TWS| ";
            default:
                return "";
        }
    }

    // EFFECTS: prints header for board display
    public void printHeader() {
        String header = "|";
        for (int i = 0; i <= 9; i++) {
            header += "_" + Integer.toString(i) + "_| ";
        }
        for (int i = 10; i < Board.BOARD_LENGTH; i++) {
            header += Integer.toString(i) + "_| ";
        }
        System.out.println(header);
        printoutSpacer();
    }
    
    // Play the game
    public static void main(String[] args) {
        new ScrabbleApp();
    }
}
