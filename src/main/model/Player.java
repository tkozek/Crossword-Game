package model;
import java.util.ArrayList;
import java.util.List;


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
    public void makeMove(List<LetterTile> letters, int startRow, int startCol, int pointsEarned) {
        //Player, Board, List<LetterTile> , Start Row, Start Col, Points Earned
        Move move = new Move(this, board, letters, startRow, startCol, pointsEarned);
        this.history.addMove(move);
    }
    // REQUIRES: 0 <= index <     public int getNumTilesOnRack() {
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
    // EFFECTS: returns true if there were tiles to clear
    //          false if player's rack was already empty.
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
    //REQUIRES: lettersToSwap must contain all and only selectedTiles
    //       and lettersToSwap.size() <= tileBag.size()
    //            and tilesToSwap.size() >= 0
    //MODIFIES: this, tileBag
    //EFFECTS: Adds specified tiles back to common draw bag, then replaces
    //          same number of tiles with random tiles from draw bag.
    public void swapTiles(List<LetterTile> lettersToSwap) {
        for (LetterTile letter : lettersToSwap) {
            this.removeLetterFromPlayer(letter);
        }
        this.tileBag.addTiles(lettersToSwap);
        this.tileBag.drawTiles(this);
    }

    //MODIFIES: this
    //EFFECTS : removes letter from player's selected
    // letters and from their tile rack
    public void removeLetterFromPlayer(LetterTile letter) {
        this.selectedTiles.remove(letter);
        this.tileRack.remove(letter);
    }
    public History getHistory() {
        return this.history;
    }
}
