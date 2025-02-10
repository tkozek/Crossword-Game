package model;
import java.util.List;


// Represents a player in the Scrabble Game

public class Player {
 
// Initializes a player with given name, zero points, no history of moves,
//   an empty tile rack, zero remaining tiles, the board they will play on.
//      Their next turn will be their first.
    public Player(String name) {

    }
    public int getNumTilesOnRack() {
        return 0;
    }
    
    public String getPlayerName() {
        return "";
    }

    public void setPlayerName() {

    }

    public int getPointsThisGame() {
        return 0;
    }
    //REQUIRES:
    //MODIFIES:
    //EFFECTS:
    public void drawTiles() {
    
    }


    //REQUIRES: letters must only be played on available board locations
    //          (multiple letters can't be placed on the same space)
    //MODIFIES: this, board
    //EFFECTS: Places letters on board beginning at start position
    //         and going in specified direction. Players points
    //         are updated and player is marked as next to draw from
    //         the tile bag.
    public void playWord() {

    }
    //REQUIRES: tilesToSwap.size() <= remainingTiles and tilesToSwap.size() <= tileBag.size()
    //            and tilesToSwap.size() >= 0
    //MODIFIES: this, tileBag
    //EFFECTS: Adds specified tiles back to common draw bag, then replaces
    //          same number of tiles with random letters from draw bag.
    public void swapTiles() {

    }
    
    public List<Move> getPlayerMoves() {
        return null;
    }

}
