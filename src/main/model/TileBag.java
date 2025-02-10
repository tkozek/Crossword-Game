package model;

// Represents the draw bag of Letter Tiles in 
// a Scrabble Game
public class TileBag {
    
    //Initializes tile bag with number of each 
    //letter standard to a Scrabble Game
    public TileBag() {

    
    }

    //REQUIRES: Tile Bag is empty
    //MODIFIES: this
    //EFFECTS: Adds correct number of each letter tile
    // and blank tile to the tile bag.
    private void initializeTiles() {

    }

    //MODIFIES: this
    //EFFECTS: Adds a new Letter Tile to the tile bag
    //          the specified number of times
    private void addTiles() {

    }

    //MODIFIES: this
    //EFFECTS: Removes random tile from tile bag and returns it,
    //            null if empty
    public LetterTile drawTile() {
        return new LetterTile();
    }

}
