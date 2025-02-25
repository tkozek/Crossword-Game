package model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistance.Writable;

public class ScrabbleGame implements Writable {
    private String name;
    private Board board;
    private TileBag tileBag;
    private History history;
    private List<Player> players;

    // Represents a Scrabble game and its assets,
    // including the current board, tile bag, 
    // move history, players involved
    public ScrabbleGame(String name, Board board, TileBag tileBag) {
        
    }

    // EFFECTS: returns this game's name
    public String getName() {
        return "";
    }

    // EFFECTS: returns number of players
    // in the game.
    public int getNumPlayers() {
        return -1;
    }

    // MODIFIES: this
    // EFFECTS: adds player to this game
    public void addPlayer(Player player) {

    }

    // EFFECTS: returns the history of all
    // moves played by players during this game
    public History getHistory() {
        return null;
    }

    // EFFECTS: returns players associated
    // with this game
    public List<Player> getPlayers() {
        return null;
    }

    // EFFECTS: Creates a JSONObject
    // from the ScrabbleGame state
    @Override
    public JSONObject toJSON() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJSON'");
    }

    // EFFECTS: returns players in this Scrabble Game 
    // as a JSONArray
    private JSONArray playersToJson() {
        return null;
    }

}
