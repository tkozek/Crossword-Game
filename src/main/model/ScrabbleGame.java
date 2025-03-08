package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import model.board.Board;
import model.move.Move;
import model.tile.LetterTile;
import model.tile.TileBag;
import persistance.Writable;

public class ScrabbleGame implements Writable {
    private String name;
    private Board board;
    private TileBag tileBag;
    private History history;
    private List<Player> players;
    private int firstPlayerIndex;

    // Represents a Scrabble game and its assets,
    // including the current board, tile bag, 
    // move history, players involved
    public ScrabbleGame(String name, Board board, TileBag tileBag) {
        this.name = name;
        this.board = board;
        this.tileBag = tileBag;
        this.history = new History();
        this.players = new ArrayList<>();
        this.firstPlayerIndex = 0;
    }

    // EFFECTS: Names this game
    public void setName(String name) {
        this.name = name;
    }

    // EFFECTS: returns this game's name
    public String getName() {
        return this.name;
    }

    // EFFECTS: returns player with given name
    // in this game
    public Player getPlayerByName(String name) {
        for (Player p : this.players) {
            if (p.getPlayerName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    // EFFECTS: returns board
    public Board getBoard() {
        return this.board;
    }
    
    // EFFECTS: returns tile bag
    public TileBag getTileBag() {
        return this.tileBag;
    }

    // EFFECTS: returns number of players
    // in the game.
    public int getNumPlayers() {
        return this.players.size();
    }

    // MODIFIES: this
    // EFFECTS: adds player to this game
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    // MODIFIES: this
    // EFFECTS: adds move to game history
    // DOES NOT add to associated player's
    // history
    public void addMove(Move move) {
        this.history.addMove(move);
    }

    // EFFECTS: returns the history of all
    // moves played by players during this game
    public History getHistory() {
        return this.history;
    }

    // EFFECTS: returns players associated
    // with this game
    public List<Player> getPlayers() {
        return this.players;
    }

    // EFFECTS: returns index of 
    // first player to play once
    // turn-taking begins
    public int getFirstPlayerIndex() {
        return firstPlayerIndex;
    }
    
    @Override
    // EFFECTS: Creates a JSONObject
    // from the ScrabbleGame state
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("GameName", this.getName());
        json.put("FirstPlayer", this.firstPlayerIndex);
        json.put("Board", this.board.toJson());
        json.put("TileBag", this.tileBag.toJson());
        json.put("Players", playersToJson());
        json.put("History", this.historyToJson());
        return json;
    }

    // EFFECTS: returns players in this Scrabble Game 
    // as a JSONArray
    private JSONArray playersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Player player : players) {
            jsonArray.put(player.toJson());
        }
        return jsonArray;
    }

    // REQUIRES: this.getPlayers() contains player
    // MODIFIES: this
    // EFFECTS: sets first player index
    // to be the index of given player
    public void setFirstPlayer(Player player) {
        this.firstPlayerIndex = players.indexOf(player);
    }

    // REQUIRES: 0 <= firstPlayerIndex < getNumPlayers()
    // MODIFIES: this
    // EFFECTS: sets first player index using index
    // of players
    public void setFirstPlayerIndex(int firstPlayerIndex) {
        this.firstPlayerIndex = firstPlayerIndex;
    }

    // EFFECTS: returns players in this Scrabble Game 
    // as a JSONArray
    private JSONArray historyToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Move move : history.getMoves()) {
            jsonArray.put(move.toJson());
        }
        return jsonArray;
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

    //REQUIRES: getSelectedTiles.size() <= tileBag.size()
    //MODIFIES: this, tileBag
    //EFFECTS: Adds specified tiles back to common draw bag, then replaces
    //          same number of tiles with random tiles from draw bag.
    //          logs this move for both the player's history and its own
    public void swapTiles(Player player) {
        String preSwapLetters = "";
        for (LetterTile letter : player.getTilesOnRack()) {
            preSwapLetters += letter.getString();
        }
        List<LetterTile> toSwap = player.copyLetterTiles(player.getSelectedTiles());
        this.tileBag.addTiles(toSwap);
        player.removeSelectedTiles();
        this.tileBag.drawTiles(player);
        String postSwapLetters = "";
        for (LetterTile letter : player.getTilesOnRack()) {
            postSwapLetters += letter.getString();
        }
        Move swap = new Move(player, preSwapLetters, postSwapLetters);
        addMoveToGameAndPlayerHistory(swap, player);
    }

    // MODIFIES: this, player
    // EFFECTS: Logs a swap into player and game's history
    public void logSwap(Player player, String initialLetters, String postSwapLetters) {
        Move swap = new Move(player, initialLetters, postSwapLetters);
        addMoveToGameAndPlayerHistory(swap, player);
    }

    // REQUIRES: Players selected tiles can be played on this board
    // starting at (row, col) and proceeding in given direction
    // MODIFIES: this, player, board, tileBag
    // EFFECTS: plays players selected tiles in desired manner on board,
    // logs the move to player and game history, replenishes player's tile
    // rack, returns score.
    public int playWord(Player player, int row, int col, Direction dir) {
        String lettersPlayed = "";
        for (LetterTile letter : player.getSelectedTiles()) {
            lettersPlayed += letter.getString();
        }
        int score = board.playWord(player.getSelectedTiles(), row, col, dir);
        Move wordPlayed = new Move(player, lettersPlayed, row, col, score, dir);
        player.addMove(wordPlayed);
        history.addMove(wordPlayed);
        player.removeSelectedTiles();
        player.addPoints(score);
        tileBag.drawTiles(player);
        return score;
    }

    // MODIFIES: this, player
    // EFFECTS: logs a previously played word to 
    // both game history and player history. 
    // DOES NOT impact score.
    public void logWord(Player player, String letters, int row, int col, int points, Direction dir) {
        Move word = new Move(player, letters, row, col, points, dir);
        addMoveToGameAndPlayerHistory(word, player);
    }

    // REQUIRES: getPlayers() contains player
    // MODIFIES: this, player
    // EFFECTS; logs a skipped turn in this history
    // and this player's history
    public void logSkippedTurn(Player player) {
        player.clearSelectedTiles();
        Move skip = new Move(player);
        addMoveToGameAndPlayerHistory(skip, player);
    }

    // MODIFIES: this
    // EFFECTS: subtracts score of unplayed tiles on the rack
    // of all players except firstToUseAllTiles, adds this pooled
    // score to firstToUseAllTiles' score. Logs these adjustments
    // in game and players' histories.
    public void performEndGameAdjustments(Player firstToUseAllTiles) {
        int total = 0;
        int playerLoss = 0;
        Move adjustment;
        String totalLetters = "";
        String letters = "";
        for (Player p : players) {
            if (p.equals(firstToUseAllTiles)) {
                continue;
            }
            playerLoss = 0;
            letters = "";
            for (LetterTile letter : p.getTilesOnRack()) {
                playerLoss += letter.getLetterPoints();
                letters += letter.getString();
            }
            p.addPoints(-1 * playerLoss);
            total += playerLoss;
            totalLetters += letters;
            adjustment = new Move(p, firstToUseAllTiles, letters, playerLoss);
            addMoveToGameAndPlayerHistory(adjustment, p);
        }
        firstToUseAllTiles.addPoints(total);
        adjustment = new Move(firstToUseAllTiles, firstToUseAllTiles, totalLetters, total);
        addMoveToGameAndPlayerHistory(adjustment, firstToUseAllTiles);
    }


    // MODIFIES: this, player
    // EFFECTS: adds this move to both the game's and the player's history
    private void addMoveToGameAndPlayerHistory(Move move, Player player) {
        history.addMove(move);
        player.addMove(move);
    }

}
