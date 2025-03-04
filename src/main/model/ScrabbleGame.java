package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.board.Board;
import model.move.Move;
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
    
}
