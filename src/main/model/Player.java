package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Represents a player in the Scrabble Game

public class Player {
 
    public static final int MAX_NUM_TILES = 7;
    private String name;
    private History history;
    private List<LetterTile> tileRack;
    private List<LetterTile> selectedTiles;
    private TileBag tileBag;
    private Board board;
    private int points;
    
// Initializes a player with given name, zero points, no history of moves,
//   an empty tile rack, no selected tiles, zero remaining tiles, the board they will play on.
//      Their next turn will be their first.
    public Player(String name, Board board, TileBag tileBag) {
        this.name = name;
        this.board = board;
        this.tileBag = tileBag;
        this.history = new History(this.name);
        this.tileRack = new ArrayList<>();
        this.selectedTiles = new ArrayList<>();
        this.points = 0;
        
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

    //EFFECTS: sets points for testing purposes
    public void setPoints(int points) {
        this.points = points;
    }

    public int getPointsThisGame() {
        return this.points;
    }

////
    //REQUIRES: 
    //MODIFIES: this, tileBag
    //EFFECTS: Draws tiles from player's tile bag 
    //         until getNumTilesOnRack() == MAX_NUM_TILES
  //  public void drawTiles() {
  //      tileBag.drawTiles(this);
 //   }
////

    // REQUIRES: getNumTilesOnRack() < MAX_NUM_TILES
    // MODIFIES: this
    //EFFECTS: Adds drawnLetter to player's tile rack
    public void addTile(LetterTile drawnLetter) {
        this.tileRack.add(drawnLetter);
    }


    // MODIFIES: this
    // EFFECTS: Adds a new word played to player's move history
    public void logWord(Board board, int startRow, int startCol, int pointsEarned, Direction dir) {
        //Player, Board, List<LetterTile> , Start Row, Start Col, Points Earned
        List<LetterTile> letters = this.getSelectedTiles();
        List<LetterTile> copy = copyLetterTiles(letters);
        Move move = new Move(this, board, copy, startRow, startCol, pointsEarned, dir);
        this.history.addMove(move);
    }

    // EFFECTS: returns deep copy of given list of letters.
    public List<LetterTile> copyLetterTiles(List<LetterTile> lettersToCopy) {
        List<LetterTile> copiedLetters = new ArrayList<>();
        for (LetterTile letter : lettersToCopy) {
            copiedLetters.add(new LetterTile(letter));
        }
        return copiedLetters;
    }

    // MODIFIES: this
    // EFFECTS: Adds a new skipped turn to player's move history
    public void logSkippedTurn(Board board) {
        history.addMove(new Move(this, board));
    }

    // MODIFIES: this
    // EFFECTS: Adds a new swap to player's move history
    public void logSwap(Board board, List<LetterTile> swappedLetters, List<LetterTile> postSwapLetters) {
        history.addMove(new Move(this, board, swappedLetters, postSwapLetters));
    }

    // REQUIRES: 0 <= index <     public int getNumTilesOnRack() {
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

    public List<LetterTile> getSelectedTiles() {
        return this.selectedTiles;
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

    
    //EFFECTS: returns chars corresponding to letters 
    //      on the player's tile rack
    public List<LetterTile> getTilesOnRack() {
        return this.tileRack;
    }

    //REQUIRES: getSelectedTiles.size() <= tileBag.size()
    //MODIFIES: this, tileBag
    //EFFECTS: Adds specified tiles back to common draw bag, then replaces
    //          same number of tiles with random tiles from draw bag.
    public void swapTiles() {
        List<LetterTile> original = this.getSelectedTiles();
        List<LetterTile> copy = new ArrayList<>();
        for (LetterTile letter : original) {
            copy.add(letter);
        }
        this.tileBag.addTiles(copy);
        this.clearSelectedTiles();
        for (LetterTile letter : copy) {
            this.tileRack.remove(letter);
        }
        this.tileBag.drawTiles(this);
    }

    //EFFECTS: returns Chacters 'A' to 'Z' and '_'
    //   mapped to their number of occurences on tile rack
    public Map<Character, Integer> getNumEachCharOnMyRack() {
        HashMap<Character,Integer> playerCharCounts = new HashMap<>();
        List<LetterTile> letters = this.getTilesOnRack();
        for (LetterTile letter : letters) {
            char letterChar = letter.getCharacter();
            playerCharCounts.put(letterChar, playerCharCounts.getOrDefault(letterChar,0) + 1);
        }
        return playerCharCounts;
    }

    // EFFECTS: Adds up total occurences of every character on board,
    // and on this player's rack. Combines those and subtracts from
    //  initial counts in draw pile to get remaining number of each
    // letter between the draw pile and opponents' racks
    public Map<Character, Integer> getNumEachCharInBagAndOpponents() {
        Map<Character, Integer> tileBagCounts = tileBag.getInitialLetterFrequencies();
        Map<Character, Integer> playerCharCounts = this.getNumEachCharOnMyRack();
        Map<Character, Integer> boardCounts = board.getNumEachCharOnBoard();
        for (Character key : tileBagCounts.keySet()) {
            int valueToSubtract = playerCharCounts.getOrDefault(key,0) + boardCounts.getOrDefault(key,0);
            tileBagCounts.put(key, tileBagCounts.get(key) - valueToSubtract);
        }
        return tileBagCounts;
    }

    public History getHistory() {
        return this.history;
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

    // MODFIFIES: this
    // EFFECTS: Adds specified number of points to 
    // players points
    public void addPoints(int points) {
        this.points += points;
    }

    //EFFECTS: returns deep copy of player's
    // selected tiles
    public List<LetterTile> copySelectedTiles() {
        return copyLetterTiles(this.getSelectedTiles());
    }
}
