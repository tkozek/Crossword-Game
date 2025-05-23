package ui;

import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import model.*;
import model.board.*;
import model.exceptions.BoardSectionUnavailableException;
import model.exceptions.InvalidLetterException;
import model.exceptions.SelectedTileOutOfBoundsException;
import model.move.Move;
import model.tile.*;
import persistance.*;
import ui.gui.SavedGameManager;
import ui.gui.ScrabbleVisualApp;

// Citation: Saving and loading are based on JSON example from edX
// Represents a game of Scrabble
public class ScrabbleConsoleApp extends ScrabbleUserInterface {

    private static final String GREETING_TEXT = "Welcome to Scrabble in Java";
    private static final String START_MENU_PROMPT = "(L)oad your old game or (p)lay a new one?";
    private static final int BOARD_LENGTH = 15;
    
    private String lastGamePath;
    private Scanner scanner;
    
    
    //EFFECTS: Creates new Scrabble Terminal App
    public ScrabbleConsoleApp() {        
        super();
        printoutSpacer();
        System.out.println(GREETING_TEXT);
        printoutSpacer();
        scanner = new Scanner(System.in);
        System.out.println(START_MENU_PROMPT);
        switch (scanner.nextLine().toLowerCase()) {
            case "p":
                initializeNewGame();
                break;
            case "l":
                loadOldGame();
                break;
            default:
                initializeNewGame();
        }
        this.gameRunning = true;
        handleGameplay();
    }

    public ScrabbleConsoleApp(ScrabbleGame game) { 
        super(); 
        this.game = game;
        this.numPlayers = game.getNumPlayers();
        this.gameRunning = true;
        scanner = new Scanner(System.in);
        handleGameplay();
    }
    
    // MODIFIES: player
    // EFFECTS: creates assets for a new game and prompts user input
    // for setup parameters
    private void initializeNewGame() {
        game = new ScrabbleGame();
        this.gameRunning = true;
        initializePlayers();
    }

    // MODIFIES: game, numPlayers
    // EFFECTS: loads assets from previously saved game
    public void loadOldGame() {
        this.gameRunning = true;
        try {
            lastGamePath = SavedGameManager.getLastGamePath();
            JsonReader jsonReader = new JsonReader(lastGamePath);
            game = jsonReader.read();
            this.numPlayers = game.getNumPlayers();
            System.out.println("Loaded game with " + String.valueOf(numPlayers) 
                    + " players from " + lastGamePath + "\n");
        } catch (IOException e) {
            System.out.println("Unable to read game from file: " + lastGamePath);
        }
    }

    // MODIFIES: game
    // EFFECTS: Prompts input for number of player's
    // and requests their names
    private void initializePlayers() {
        System.out.println("Please enter the number of players [1,4]");
        int numPlayers = this.scanner.nextInt();
        String numPlayerConfirmation = Integer.toString(numPlayers);
        System.out.println("You entered " + numPlayerConfirmation 
                + " players. Is that correct? Press (Y) to confirm or (N) to cancel and re-enter");
        scanner.nextLine();
        String confirmOrCancel = this.scanner.nextLine().toUpperCase();
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
    
    // MODIFIES: this
    // EFFECTS: Prompts player to enter players' names
    //         in the desired order of play.
    private void requestPlayerNames(int numPlayers) {
        System.out.println(
                "Please enter each player's name separately, in the order you want to play");
        while (game.getNumPlayers() < numPlayers) {
            String inputPlayerName = scanner.nextLine();
            System.out.println("You entered " 
                    + inputPlayerName + ". Press (Y) to confirm or (N) to cancel and re-enter");
            String confirmName = scanner.nextLine().toUpperCase();
            if (confirmName.equals("Y")) {
                game.addPlayer(inputPlayerName);
            } else if (confirmName.equals("N")) {
                System.out.println("Okay, name wasn't saved. Please re-enter the correct name");
            } else {
                System.out.println("Invalid confirmation response. Please re-enter the desired name");
            }
            printoutSpacer();
        }
    }

    // MODIFIES: game
    // EFFECTS: Manages order of turn taking, 
    // and ensures player's draw new tiles
    // when they are supposed to
    private void handleGameplay() {
        int index;
        while (gameRunning) {
            for (int i = 0; i < numPlayers; i++) {
                index = (i + game.getCurrentPlayerIndex()) % numPlayers;
                getBoardPrintOut(game.getBoard());
                Player playerToPlayNext = game.getPlayerByIndex(index);
                game.drawTiles(playerToPlayNext);
                handleTurn(playerToPlayNext);
                if (playerToPlayNext.outOfTiles()) {
                    handleEndGame(playerToPlayNext);
                }
                if (!gameRunning) {
                    break;
                }
            }
        }
    }

    // MODIFIES: player, board
    // EFFECTS: prompts player to decide which 
    // type their next move will be, or if they
    // want to view something
    private void handleTurn(Player player) {
        System.out.println("\n Here are your tiles: " + player.getPlayerName());
        getTilePrintOut(player);
        System.out.println("Type whether you'd like to (P)lay, (S)wap, S(k)ip, or (o)ther.");
        switch (scanner.nextLine().toLowerCase()) {
            case "p":
                handlePlay(player);
                scanner.nextLine();
                break;
            case "s":
                handleSwap(player);
                scanner.nextLine();
                break;
            case "k":
                handleSkip(player);
                scanner.nextLine();
                break;
            case "o":
                handleNonPlayOptions(player);
                break;
            default:
                handleTurn(player);
        }
    }

    // MODIFIES: this
    // EFFECTS: Let's player decide to view
    // game related info, or save and quit
    private void handleNonPlayOptions(Player player) {
        System.out.println("\nType whether you'd like to (v)iew something, (s)witch to GUI, s(a)ve and quit, "
                + "or (q)uit without saving");
        switch (scanner.nextLine().toLowerCase()) {
            case "v":
                handleViewings(player);
                break;
            case "s":
                game.setCurrentPlayer(player);
                this.gameRunning = false;
                new ScrabbleVisualApp(game);
                try {
                    Object lock = new Object();
                    lock.notifyAll();
                } catch (IllegalMonitorStateException e) {
                    //
                }
                break;
            case "a":
                handleSave(player);
                this.gameRunning = false;
                printEventLog();
                System.exit(0);
            case "q":
            // !!! Consider adding confirmation
                this.gameRunning = false;
                printEventLog();
                System.exit(0);
            default:
                handleNonPlayOptions(player);
        }
    }

    // EFFECTS: Prompts user for what they
    // would like to view about the game, displays that.
    private void handleViewings(Player player) {
        System.out.println("\n Type whether you'd like to view current (b)oard, (w)ords played, all (m)oves,"
                + " (f)iltered words, (r)emaining tile counts, or anything else to cancel");
        switch (scanner.nextLine().toLowerCase()) {
            case "b":
                getBoardPrintOut(game.getBoard());
                break;
            case "w":
                printWordsPlayed(player);
                break;
            case "m":
                printAllMovesSummary(player);
                break;
            case "f":
                handleShowFilteredMoves(player);
                break;
            case "r":
                printLetterDistribution(player);
                break;
        }
        handleTurn(player);
    }

    // EFFECTS: Returns summary of all words player has played
    // which contains their input letter, or indicates of 
    // no such words exist.
    private void handleShowFilteredMoves(Player player) {
        boolean validInput = false;
        List<Move> moves = null;
        char entry = ' ';
        while (!validInput) {
            System.out.println("Enter a character to view all your words played which contained that character");
            String input = scanner.nextLine().toUpperCase();
            if (input.isEmpty()) {
                System.out.println("Empty selection is invalid \n");
                continue;
            }
            entry = input.charAt(0);
            try {
                moves = player.getWordsContainingLetter(entry);
                validInput = true;
            } catch (InvalidLetterException e) {
                System.out.println(e.getMessage());
            }
        }
        if (moves.isEmpty()) {
            System.out.println("You haven't played any words containing " + String.valueOf(entry));
        } else {
            for (Move move : moves) {
                System.out.println(game.getWordDescription(move, player));
            }
        }
        scanner.nextLine();
    }

    // MODIFIES: game
    // EFFECTS: Saves game to file
    private void handleSave(Player player) {
        try {
            lastGamePath = SavedGameManager.getLastGamePath();
            JsonWriter jsonWriter = new JsonWriter(lastGamePath);
            game.setCurrentPlayer(player);
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            System.out.println("Saved game with " + String.valueOf(numPlayers)
                    + " players to " + lastGamePath);
        } catch (IOException e) {
            System.out.println("Unable to write to file " + lastGamePath);
        }
    }

    // EFFECTS: Prints all player moves
    // in order, including playing words, swaps, skips
    private void printAllMovesSummary(Player player) {
        String description = "";
        for (Move move : player) {
            switch (move.getMoveType()) {
                case PLAY_WORD:
                    description = game.getWordDescription(move, player);
                    break;
                case SWAP_TILES:
                    description = game.getSwapDescription(move, player);
                    break;
                case SKIP:
                    description = game.getSkipDescription(move, player);
                    break;
                case END_GAME_ADJUSTMENT:
                    description = game.getEndGameDescription(move, player);
                    break;
            }
            System.out.println(description);
        }
        scanner.nextLine();
    }

    // EFFECTS: prints summary of all
    // words played by the player.
    private void printWordsPlayed(Player player) {
        List<Move> wordsPlayed = player.getWordsPlayed();
        for (Move word : wordsPlayed) {
            System.out.println(game.getWordDescription(word, player));
        }
    }

    // MODIFIES: player, Board, TileBag
    // EFFECTS: Prompts player to input directions
    // to place a word on the board
    private void handlePlay(Player player) {
        while (true) {
            System.out.println("Enter the index of the tiles you'd like to play, in the order they form your word "
                    + "or enter C to confirm");
            if (scanner.hasNextInt()) {
                try {
                    player.selectTile(scanner.nextInt() - 1);
                } catch (SelectedTileOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
                printSelectedTilesMessage(player);
            } else if (scanner.hasNext("C")) {
                handlePositionAndDirectionSelection(player);
                break;
            } else {
                System.out.println("Invalid entry");
                handleTurn(player);
            }
        }
    }

    // MODIFIES: game, player
    // EFFECTS: Prompts player for position and direction 
    // instructions, then places and logs the turn
    // their selected tiles as directed 
    // OR 
    // prints error message if instructions were invalid
    private void handlePositionAndDirectionSelection(Player player) {
        adjustScanner();
        boolean validInput = false;
        int row;
        int col;
        Direction dir;
        int score = 0;
        while (!validInput) {
            System.out.println("Enter the row index you'd like to start your word at \n ");
            row = scanner.nextInt();
            System.out.println("Enter the column index you'd like to start your word at");
            col = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Now enter direction (R)ight or (D)own (default)");
            dir = (scanner.nextLine().toLowerCase().equals("r")) ? Direction.RIGHT : Direction.DOWN;
            try {
                score = game.playWord(player, row, col, dir);
                validInput = true;
            } catch (BoardSectionUnavailableException e) {
                System.out.println(e.getMessage());
            }
        }
        printPlayerSummaryAfterWordPlayed(player, score);
        // System.out.println("Can't play that word there");
        // player.clearSelectedTiles();
        // handlePlay(player);
    }

    private void printPlayerSummaryAfterWordPlayed(Player player, int score) {
        System.out.println("\nYour new tiles are:");
        getTilePrintOut(player);
        System.out.println(player.getPlayerName() + " earned " + score + " points!");
        System.out.println(player.getPlayerName() + " now has " + player.getPointsThisGame() + " points \n");
    }

    // MODIFIES: player, tileBag
    // EFFECTS: Prompts player to 
    // select tiles to swap, swaps and displays 
    // new tiles on confirmation, logs the swap
    private void handleSwap(Player player) {
        while (true) {
            System.out.println("Enter indices for tiles to swap, C to confirm, or any other character to cancel");
            if (scanner.hasNextInt()) {
                player.selectTile(scanner.nextInt() - 1);
                printSelectedTilesMessage(player);
            } else if (scanner.hasNext("C")) {
                game.swapTiles(player);
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

    // MODIFIES: game, player
    // EFFECTS: deselects players tiles
    // logs the skipped turn and prints confirmation
    private void handleSkip(Player player) {
        game.logSkippedTurn(player);
        System.out.println(player.getPlayerName() + " skipped their turn \n");
    }

    //EFFECTS: Prints out selected tiles for player
    private void printSelectedTilesMessage(Player player) {
        System.out.println("So far you've selected: ");
        String tilePrintOut = "";
        List<LetterTile> playersSelectedLetters = player.getSelectedTiles();
        for (LetterTile letter : playersSelectedLetters) {
            tilePrintOut += letter.toDisplay();
        }
        System.out.println(tilePrintOut + "\n");
    }

    //EFFECTS: Prints out all tiles on the player's tile rack
    private void getTilePrintOut(Player player) {
        String tilePrintOut = "";
        List<LetterTile> playersLetters = player.getTilesOnRack();
        for (LetterTile letter : playersLetters) {
            tilePrintOut += letter.toDisplay();
        }
        System.out.println(tilePrintOut + "\n");
    }

    // EFFECTS: Prints remaining character counts
    // for tiles not on the board or the player's tile rack
    private void printLetterDistribution(Player player) {
        printoutSpacer();
        System.out.println("The remaining tile counts in the format 'Letter : Count' are:");
        Map<Character, Integer> remainingCounts = game.getNumEachCharInBagAndOpponents(player);
        for (Map.Entry<Character, Integer> entry : remainingCounts.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        printoutSpacer();
    }

    // EFFECTS: Prints a separator to the console
    private void printoutSpacer() {
        String spacer = "";
        for (int i = 0; i < 74; i++) {
            spacer += "-";
        }
        System.out.println(spacer);
    }

    // EFFECTS: prints out the current board
    private void getBoardPrintOut(Board board) {
        printHeader();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            String rowPrintout = "";
            for (int j = 0; j < BOARD_LENGTH; j++) {
                Tile tile = game.getTileAtPositionOnBoard(i,j);
                rowPrintout += tile.getTerminalPrintoutString();
            }
            System.out.println(rowPrintout + "|" + Integer.toString(i) +  "\n");
        }
    }

    // EFFECTS: prints header for board display
    private void printHeader() {
        String header = "\n|";
        // Need to adjust based on single digit or two digit row/column
        // so that elements are aligned with board display
        for (int i = 0; i <= 9; i++) {
            header += "_" + Integer.toString(i) + "_| ";
        }
        for (int i = 10; i < BOARD_LENGTH; i++) {
            header += Integer.toString(i) + "_| ";
        }
        System.out.println(header);
        printoutSpacer();
    }

    // Play the game
    public static void main(String[] args) {
        new ScrabbleConsoleApp();
    }
}
