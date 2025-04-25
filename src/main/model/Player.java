package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import model.move.Move;
import model.move.MoveType;
import model.tile.LetterTile;
import persistance.JsonWritable;

// Represents a player in the Scrabble Game

public class Player implements JsonWritable<JSONObject>, Iterable<Move> {
 
    private String name;
    private History history;
    private List<LetterTile> tileRack;
    private List<LetterTile> selectedTiles;
    private int points;
    
// Initializes a player with given name, zero points, no history of moves,
//   an empty tile rack, no selected tiles, zero remaining tiles, the game they will play in.
//      Their next turn will be their first.
    public Player(String name) {
        this.name = name;
        this.history = new History();
        this.tileRack = new ArrayList<>();
        this.selectedTiles = new ArrayList<>();
        this.points = 0;   
    }

    // EFFECTS: represents this player
    // as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("score", this.points);
        json.put("tileRack", tileRackToJson());
        return json;
    }

    @Override
    public Iterator<Move> iterator() {
        return history.iterator();
    }   

    // EFFECTS: returns deep copy of given list of letters.
    public List<LetterTile> copyLetterTiles(List<LetterTile> lettersToCopy) {
        List<LetterTile> copiedLetters = new ArrayList<>();
        for (LetterTile letter : lettersToCopy) {
            copiedLetters.add(new LetterTile(letter));
        }
        return copiedLetters;
    }

    // REQUIRES: 0 <= index < getNumTilesOnRack() {
    // MODIFIES: this
    // EFFECTS: returns true if tile at index
    //          was successfully selected
    public boolean selectTile(int index) {
        if (!selectedTiles.contains(tileRack.get(index))) {
            this.selectedTiles.add(tileRack.get(index));
            return true;
        }
        return false;
    }


    // EFFECTS: returns true iff
    // there are no tiles on this player's rack
    public boolean outOfTiles() {
        return tileRack.isEmpty();
    }

    // MODIFIES: this
    // EFFECTS: removes all selected tiles
    // from tile rack, and clears selected tiles
    public void removeSelectedTiles() {
        List<LetterTile> selectedTiles = this.getSelectedTiles();
        for (LetterTile letter : selectedTiles) {
            tileRack.remove(letter);
        }
        clearSelectedTiles();
    }

    // MODIFIES: this
    // EFFECTS: clears selected tiles if any were selected
    // returns true if there were tiles to clear 
    // false if player's rack was already empty.
    public boolean clearSelectedTiles() {
        if (this.selectedTiles.isEmpty()) {
            return false;
        }
        this.selectedTiles.clear();
        return true;
    }

    // REQUIRES: getNumTilesOnRack() < MAX_NUM_TILES
    // MODIFIES: this
    //EFFECTS: Adds drawnLetter to player's tile rack
    public void addTile(LetterTile drawnLetter) {
        this.tileRack.add(drawnLetter);
    }

    // MODFIFIES: this
    // EFFECTS: Adds specified number of points to 
    // players points
    public void addPoints(int points) {
        this.points += points;
    }

    // MODIFIES: this
    // EFFECTS: adds move to this player's history
    public void addMove(Move move) {
        history.addMove(move);
    }

    //EFFECTS: returns deep copy of player's
    // selected tiles
    public List<LetterTile> copySelectedTiles() {
        return copyLetterTiles(this.getSelectedTiles());
    }

    public List<LetterTile> getSelectedTiles() {
        return this.selectedTiles;
    }

    // REQUIRES: letter is uppercase,
    //      between 'A' to 'Z' or '-'
    // EFFECTS: Filters player's words played and returns
    // them in order least to most recent, only including
    // moves which used the given letter at least once
    public List<Move> getWordsContainingLetter(char letter) {
        return history.getWordsContainingLetter(letter);
    }

    // EFFECTS: Returns only the moves where a word was 
    // played. Excludes any turns which were swaps
    // or skips
    public List<Move> getWordsPlayed() {
        return history.getWordsPlayed();
    }

    //EFFECTS: returns chars corresponding to letters 
    //      on the player's tile rack
    public List<LetterTile> getTilesOnRack() {
        return this.tileRack;
    }

    public History getHistory() {
        return this.history;
    }

    public List<Move> getMoves() {
        return this.history.getMoves();
    }

    public int getNumTilesOnRack() {
        return tileRack.size();
    }
    
    public String getPlayerName() {
        return this.name;
    }

    // EFFECTS: updates player's name, does
    // not update their entries in history or
    // history name
    public void setPlayerName(String name) {
        this.name = name;
    }

    public int getPointsThisGame() {
        return this.points;
    }

    //EFFECTS: sets points for testing purposes
    public void setPoints(int points) {
        this.points = points;
    }

    //EFFECTS: returns Chacters 'A' to 'Z' and '_'
    //   mapped to their number of occurences on tile rack
    public Map<Character, Integer> getNumEachCharOnMyRack() {
        HashMap<Character,Integer> playerCharCounts = new HashMap<>();
        Set<LetterTile> letters = new HashSet<>(this.getTilesOnRack());
        for (LetterTile letter : letters) {
            char letterChar = letter.getCharacter();
            playerCharCounts.put(letterChar, playerCharCounts.getOrDefault(letterChar,0) + 1);
        }
        return playerCharCounts;
    }

    // EFFECTS: returns JSONArray representing
    // tiles on this player's rack
    private JSONArray tileRackToJson() {
        JSONArray json = new JSONArray();
        for (LetterTile letter : tileRack) {
            json.put(letter.toJson());
        }
        return json;
    } 
}
