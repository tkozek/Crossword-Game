package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import model.board.Board;
import model.event.Event;
import model.event.EventLog;
import model.exceptions.BoardSectionUnavailableException;
import model.move.Move;
import model.tile.LetterTile;
import model.tile.Tile;
import model.tile.TileBag;
import persistance.JsonWritable;

public class ScrabbleGame implements JsonWritable<JSONObject> {
    private Board board;
    private TileBag tileBag;
    private History history;
    private List<Player> players;
    private int currentPlayerIndex;
    private int startRow = 7;
    private int startCol = 7;
    private Direction dir = Direction.DOWN;

    // Represents a Scrabble game and its assets,
    // including the current board, tile bag, 
    // move history, players involved
    public ScrabbleGame(Board board, TileBag tileBag) {
        this.board = board;
        this.tileBag = tileBag;
        this.history = new History();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    public ScrabbleGame() {
        this.board = new Board();
        this.tileBag = new TileBag();
        this.history = new History();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }
    
    // EFFECTS: Creates a JSONObject from this
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("CurrentPlayer", this.currentPlayerIndex);
        json.put("Board", this.board.toJson());
        json.put("TileBag", this.tileBag.toJson());
        json.put("Players", playersToJson());
        json.put("History", this.historyToJson());
        return json;
    }


    // REQUIRES: Players selected tiles can be played on this board
    // starting at (startRow, startCol) and proceeding in given direction
    // MODIFIES: this, player, board, tileBag, EventLog
    // EFFECTS: plays players selected tiles in desired manner on board,
    // logs the move to EventLog, player history and game history. Replenishes player's tile
    // rack, returns score.
    public int playWord(Player player) throws BoardSectionUnavailableException {
        String lettersPlayed = "";
        for (LetterTile letter : player.getSelectedTiles()) {
            lettersPlayed += letter.toDisplay();
        }
        int score = board.playWord(player.getSelectedTiles(), startRow, startCol, dir);
        if (lettersPlayed.length() == 7) {
            score += 50;
        }
        Move wordPlayed = new Move(player.getPlayerName(), lettersPlayed, startRow, startCol, score, dir);
        updateHistoriesAndEventLog(wordPlayed, player);
        player.removeSelectedTiles();
        player.addPoints(score);
        tileBag.drawTiles(player);
        return score;
    }

    // REQUIRES: Players selected tiles can be played on this board
    // starting at (row, col) and proceeding in given direction
    // MODIFIES: this, player, board, tileBag, EventLog
    // EFFECTS: plays players selected tiles in desired manner on board,
    // logs the move to EventLog, player history and game history. Replenishes player's tile
    // rack, returns score.
    public int playWord(Player player, int row, int col, Direction dir) throws BoardSectionUnavailableException {
        String lettersPlayed = "";
        for (LetterTile letter : player.getSelectedTiles()) {
            lettersPlayed += letter.toDisplay();
        }
        int score = board.playWord(player.getSelectedTiles(), row, col, dir);
        Move wordPlayed = new Move(player.getPlayerName(), lettersPlayed, row, col, score, dir);
        updateHistoriesAndEventLog(wordPlayed, player);
        player.removeSelectedTiles();
        player.addPoints(score);
        tileBag.drawTiles(player);
        return score;
    }

    //REQUIRES: number of selected tiles <= tileBag.size()
    //MODIFIES: this, tileBag
    //EFFECTS: Adds specified tiles back to common draw bag, then replaces
    //          same number of tiles with random tiles from draw bag.
    //          logs this move for both the player's history and its own
    public void swapTiles() {
        swapTiles(getCurrentPlayer());
    }

    //REQUIRES: number of selected tiles <= tileBag.size()
    //MODIFIES: this, tileBag
    //EFFECTS: Adds specified tiles back to common draw bag, then replaces
    //          same number of tiles with random tiles from draw bag.
    //          logs this move for both the player's history and its own
    public void swapTiles(Player player) {
        String preSwapLetters = "";
        for (LetterTile letter : player.getTilesOnRack()) {
            preSwapLetters += letter.toDisplay();
        }
        List<LetterTile> toSwap = player.copyLetterTiles(player.getSelectedTiles());
        this.tileBag.addTiles(toSwap);
        player.removeSelectedTiles();
        this.tileBag.drawTiles(player);
        String postSwapLetters = "";
        for (LetterTile letter : player.getTilesOnRack()) {
            postSwapLetters += letter.toDisplay();
        }
        Move swap = new Move(player.getPlayerName(), preSwapLetters, postSwapLetters);
        updateHistoriesAndEventLog(swap, player);
    }

    // MODIFIES: this
    // EFFECTS: fills players tile rack with as
    // many tiles as allowed from the game's tilebag
    public void drawTiles(Player player) {
        tileBag.drawTiles(player);
    }

    // MODIFIES: this
    // EFFECTS: subtracts score of unplayed tiles on the rack
    // of all players except the one who played all their tiles, adds this pooled
    // score to the last player's score. Logs these adjustments
    // in game and players' histories.
    public void performEndGameAdjustments(Player lastPlayer) {
        int total = 0;
        int playerLoss = 0;
        Move adjustment;
        String totalLetters = "";
        String letters = "";
        for (Player player : players) {
            if (!(player.equals(lastPlayer))) {
                playerLoss = 0;
                letters = "";
                for (LetterTile letter : player.getTilesOnRack()) {
                    playerLoss += letter.getPoints();
                    letters += letter.toDisplay();
                }
                player.addPoints(-1 * playerLoss);
                total += playerLoss;
                totalLetters += letters;
                adjustment = new Move(player.getPlayerName(), lastPlayer.getPlayerName(), letters, -1 * playerLoss);
                updateHistoriesAndEventLog(adjustment, player);
            }
        }
        lastPlayer.addPoints(total);
        Move move = new Move(lastPlayer.getPlayerName(), lastPlayer.getPlayerName(), totalLetters, total);
        updateHistoriesAndEventLog(move, lastPlayer);
    }

    // MODIFIES: this, player
    // EFFECTS: logs a previously played word to 
    // both game history and player history. 
    // DOES NOT impact score.
    public void logWord(Player player, String letters, int row, int col, int points, Direction dir) {
        Move word = new Move(player.getPlayerName(), letters, row, col, points, dir);
        updateHistories(word, player);
    }

    // MODIFIES: this, player
    // EFFECTS: Logs a swap into player and game's history
    public void logSwap(Player player, String initialLetters, String postSwapLetters) {
        Move swap = new Move(player.getPlayerName(), initialLetters, postSwapLetters);
        updateHistories(swap, player);
    }

    // REQUIRES: getPlayers() contains player
    // MODIFIES: this, player, EventLog
    // EFFECTS; logs a skipped turn in this history
    // and this player's history, and the EventLog
    public void logSkippedTurn() {
        logSkippedTurn(getCurrentPlayer());
    }

    // REQUIRES: getPlayers() contains player
    // MODIFIES: this, player, EventLog
    // EFFECTS; logs a skipped turn in this history
    // and this player's history, and the EventLog
    public void logSkippedTurn(Player player) {
        player.clearSelectedTiles();
        Move skip = new Move(player.getPlayerName());
        updateHistoriesAndEventLog(skip, player);
    }

    // REQUIRES: getPlayers() contains player
    // MODIFIES: this, player
    // EFFECTS; logs a skipped turn in this history and this player's history
    public void logSkipFromHistory(Player player) {
        player.clearSelectedTiles();
        Move skip = new Move(player.getPlayerName());
        updateHistories(skip, player);
    }

    // MODIFIES: this, player
    // EFFECTS: Logs an end game adjustment into player and game's history
    public void logEndGameAdjustment(Player player, Player lastPlayer, String letters, int pointChange) {
        Move endGameAdjustment = new Move(player.getPlayerName(), lastPlayer.getPlayerName(), letters, pointChange);
        updateHistoriesAndEventLog(endGameAdjustment, player);
    }

    public String[][] previewBoardDisplay() throws BoardSectionUnavailableException {
        return previewBoardDisplay(getCurrentPlayer());
    }

    public String[][] previewBoardDisplay(Player player) throws BoardSectionUnavailableException {
        Board copyBoard = new Board(board);
        copyBoard.playWord(player.getSelectedTiles(), startRow, startCol, dir);
        String[][] previewDisplay = new String[Board.BOARD_LENGTH][Board.BOARD_LENGTH];
        for (int i = 0; i < Board.BOARD_LENGTH; i++) {
            for (int j = 0; j < Board.BOARD_LENGTH; j++) {
                previewDisplay[i][j] = copyBoard.getTileAtPositionOnBoard(i, j).toDisplay();
            }
        }
        return previewDisplay;
    }
    
    
    // REQUIRES: MoveType is PLAY_WORD
    // EFFECTS: returns summary of a word played
    public String getWordDescription(Move word, Player player) {
        String printout = player.getPlayerName() + " played ";
        String wordString = word.getLettersInvolved();
        String startRow = String.valueOf(word.getStartRow());
        String startCol = String.valueOf(word.getStartColumn());
        String coordinates = "(" + startRow + "," + startCol + ")";
        String direction = (word.getDirection() == Direction.RIGHT) ? "to the right" : "down";
        String points = String.valueOf(word.getPointsForMove());
        printout += wordString + " starting at " + coordinates + " and moving " 
                + direction + ", earning " + points + " points.";
        return printout;
    }


    // REQUIRES: MoveType is SWAP_TILES
    //EFFECTS: returns summary of a player swap
    public String getSwapDescription(Move swap, Player player) {
        String printout = player.getPlayerName() + " swapped tiles. ";
        String preAndPostLetters = swap.getLettersInvolved();
        int halfLength = preAndPostLetters.length() / 2;
        String preSwapLetters = preAndPostLetters.substring(0, halfLength);
        String postSwapLetters = preAndPostLetters.substring(halfLength);
        String points = String.valueOf(swap.getPointsForMove());
        printout += "Their tiles before swapping were: " + preSwapLetters + " and their tiles after swapping were " 
                 + postSwapLetters + ", earning " + points + " points.";
        return printout;
    }



    // REQUIRES: MoveType is SKIP
    // EFFECTS: returns summary of a skipped turn
    public String getSkipDescription(Move skip, Player player) {
        return player.getPlayerName() + " skipped their turn.";
    }

    // REQUIRES: MoveType is END_GAME_ADJUSTMENT
    // EFFECTS: returns summary of a end game adjustment
    public String getEndGameDescription(Move move, Player player) {
        String playerName = player.getPlayerName();
        String lastPlayer = move.getLastPlayerName();
        int pointChange = move.getPointsForMove();
        int absolutePointChange = Math.abs(pointChange);
        String gainOrLoss = (pointChange >= 0) ? " gained " : " lost ";
        String pluralOrNot = (absolutePointChange != 1) ? "s" : "";
        return lastPlayer + " used all their tiles first. \n" 
                + playerName + gainOrLoss + absolutePointChange + " point" + pluralOrNot;
    }

    public Map<String, Integer> getPlayerScoreMap() {
        Map<String, Integer> scores = new LinkedHashMap<>();
        for (Player player : players) {
            scores.put(player.getPlayerName(), player.getPointsThisGame());
        }
        return scores;
    }

    public String getCensoredLastMoveDescription() {
        Move mostRecentMove = history.getLastMove();
        Player mostRecentPlayer = getPlayerByName(mostRecentMove.getPlayerName());
        switch (mostRecentMove.getMoveType()) {
            case SWAP_TILES:
                return mostRecentPlayer.getPlayerName() + " swapped their tiles.";
            case SKIP:
                return mostRecentPlayer.getPlayerName() + " skipped their turn.";
            default:
                return getMoveDescription(mostRecentMove);
        }
    }

    public String getMoveDescription(Move move) {
        String summary = "";
        Player player = getPlayerByName(move.getPlayerName());
        switch (move.getMoveType()) {
            case PLAY_WORD:
                summary = getWordDescription(move, player);
                break;
            case SWAP_TILES:
                summary = getSwapDescription(move, player);
                break;
            case SKIP:
                summary = getSkipDescription(move, player);
                break;
            case END_GAME_ADJUSTMENT:
                summary = getEndGameDescription(move, player);
                break;
        }
        return summary;
    }

    // EFFECTS: Adds up total occurences of every character on board,
    // and on this player's rack. Combines those and subtracts from
    // initial counts in draw pile to get remaining number of each
    // letter between the draw pile and opponents' racks
    public Map<Character, Integer> getNumEachCharInBagAndOpponents(Player player) {
        Map<Character, Integer> tileBagCounts = tileBag.getInitialLetterFrequencies();
        Map<Character, Integer> playerCharCounts = player.getNumEachCharOnMyRack();
        Map<Character, Integer> boardCounts = board.getNumEachCharOnBoard();
        for (Character key : tileBagCounts.keySet()) {
            int valueToSubtract = playerCharCounts.getOrDefault(key,0) + boardCounts.getOrDefault(key,0);
            tileBagCounts.put(key, tileBagCounts.get(key) - valueToSubtract);
        }
        return tileBagCounts;
    }

    // MODIFIES: this
    // EFFECTS: adds player to this game
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    // MODIFIES: this
    // EFFECTS: adds player to this game
    public void addPlayer(String name) {
        this.players.add(new Player(name));
    }

    // MODIFIES: this
    // EFFECTS: adds move to game history
    // DOES NOT add to associated player's history
    public void addMove(Move move) {
        this.history.addMove(move);
    }

    // EFFECTS: returns the player with the highest score in this game
    public Player getHighestScoringPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        Player highestScoringPlayer = players.get(0);
        int highest = highestScoringPlayer.getPointsThisGame();
        for (Player player : players) {
            if (player.getPointsThisGame() > highest) {
                highestScoringPlayer = player;
            }
        }
        return highestScoringPlayer;
    }

    public Player getPlayerByIndex(int index) {
        return players.get(index % players.size());
    }

    // EFFECTS: returns player with given name in this game
    public Player getPlayerByName(String name) {
        for (Player player : this.players) {
            if (player.getPlayerName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public Tile getTileAtPositionOnBoard(int row, int column) {
        return board.getTileAtPositionOnBoard(row, column);
    }

    // EFFECTS: returns board
    public Board getBoard() {
        return this.board;
    }
    
    // EFFECTS: returns tile bag
    public TileBag getTileBag() {
        return this.tileBag;
    }

    // EFFECTS: returns players associated with this game
    public List<Player> getPlayers() {
        return this.players;
    }

    // EFFECTS: returns index of current player
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public int getPlayerIndex(Player player) {
        return players.indexOf(player);
    }

    public void setStartRow(int startRow) { 
        this.startRow = startRow;
    }

    public void setStartCol(int startCol) { 
        this.startCol = startCol;
    }

    public Direction getDirection() {
        return this.dir;
    }

    public void setDirection(Direction dir) {
        this.dir = dir;
    }

    public int getStartRow() { 
        return startRow;    
    }

    public int getStartCol() { 
        return startCol;
    }

    public Player getCurrentPlayer() {
        return this.players.get(currentPlayerIndex);
    }

    // REQUIRES: this.getPlayers() contains player
    // MODIFIES: this
    // EFFECTS: sets first player index to be the index of given player
    public void setCurrentPlayer(Player player) {
        this.currentPlayerIndex = players.indexOf(player);
    }

    // MODIFIES: this
    // EFFECTS: sets first player index using index of players
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        if (!players.isEmpty()) {
            this.currentPlayerIndex = currentPlayerIndex % players.size();
        } else {
            this.currentPlayerIndex = 0;
        }
    }

    public void nextPlayer() {
        setCurrentPlayerIndex(currentPlayerIndex + 1);
    }

    // EFFECTS: returns number of players in the game.
    public int getNumPlayers() {
        return this.players.size();
    }

    public List<Move> getMoves() {
        return history.getMoves();
    }

    // MODIFIES: this, player, EventLog
    // EFFECTS: adds this move to both the game's and the player's history,
    // and adds relevant Event to EventLog.
    private void updateHistoriesAndEventLog(Move move, Player player) {
        history.addMove(move);
        player.addMove(move);
        String description = "";
        switch (move.getMoveType()) {
            case PLAY_WORD:
                description = getWordDescription(move, player);
                break;
            case SWAP_TILES:
                description = getSwapDescription(move, player);
                break;   
            case SKIP:
                description = getSkipDescription(move, player);
                break;
            default:
                description = getEndGameDescription(move, player);
        }
        EventLog log = EventLog.getInstance();
        log.logEvent(new Event(description));
    }

    // MODIFIES: this, player
    // EFFECTS: adds this move to both the game's and the player's history
    private void updateHistories(Move move, Player player) {
        history.addMove(move);
        player.addMove(move);
    }

    // EFFECTS: returns players in this Scrabble Game as a JSONArray
    private JSONArray playersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Player player : players) {
            jsonArray.put(player.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns players in this Scrabble Game as a JSONArray
    private JSONArray historyToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Move move : history) {
            jsonArray.put(move.toJson());
        }
        return jsonArray;
    }
}