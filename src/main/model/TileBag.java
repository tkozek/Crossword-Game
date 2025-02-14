package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import java.util.Random;

// Represents the draw bag of Letter Tiles in 
// a Scrabble Game
public class TileBag {
    private static final  Map<Character, Integer> LETTER_FREQUENCIES = new HashMap<>();
    private static final  Map<Character, Integer> LETTER_POINTS = new HashMap<>();
    private List<LetterTile> drawPile;
    private Random randomIndexGenerator;
    

    //Initializes tile bag with number of each 
    //letter standard to a Scrabble Game
    public TileBag() {
        drawPile = new ArrayList<>();
        initializeMaps();
        initializeTiles();
        randomIndexGenerator = new Random();
    }
    private void initializeMaps() {
        LETTER_FREQUENCIES.put('A', 9); LETTER_FREQUENCIES.put('B', 2); LETTER_FREQUENCIES.put('C', 2);
        LETTER_FREQUENCIES.put('D', 4); LETTER_FREQUENCIES.put('E', 12); LETTER_FREQUENCIES.put('F', 2);
        LETTER_FREQUENCIES.put('G', 3); LETTER_FREQUENCIES.put('H', 2); LETTER_FREQUENCIES.put('I', 9);
        LETTER_FREQUENCIES.put('J', 1); LETTER_FREQUENCIES.put('K', 1); LETTER_FREQUENCIES.put('L', 4);
        LETTER_FREQUENCIES.put('M', 2); LETTER_FREQUENCIES.put('N', 6); LETTER_FREQUENCIES.put('O', 8);
        LETTER_FREQUENCIES.put('P', 2); LETTER_FREQUENCIES.put('Q', 1); LETTER_FREQUENCIES.put('R', 6);
        LETTER_FREQUENCIES.put('S', 4); LETTER_FREQUENCIES.put('T', 6); LETTER_FREQUENCIES.put('U', 4);
        LETTER_FREQUENCIES.put('V', 2); LETTER_FREQUENCIES.put('W', 2); LETTER_FREQUENCIES.put('X', 1);
        LETTER_FREQUENCIES.put('Y', 2); LETTER_FREQUENCIES.put('Z', 1); LETTER_FREQUENCIES.put('-', 2);

        LETTER_POINTS.put('A', 1); LETTER_POINTS.put('B', 3); LETTER_POINTS.put('C', 3);
        LETTER_POINTS.put('D', 2); LETTER_POINTS.put('E', 1); LETTER_POINTS.put('F', 4);
        LETTER_POINTS.put('G', 2); LETTER_POINTS.put('H', 4); LETTER_POINTS.put('I', 1);
        LETTER_POINTS.put('J', 8); LETTER_POINTS.put('K', 5); LETTER_POINTS.put('L', 1);
        LETTER_POINTS.put('M', 3); LETTER_POINTS.put('N', 1); LETTER_POINTS.put('O', 1);
        LETTER_POINTS.put('P', 3); LETTER_POINTS.put('Q', 10); LETTER_POINTS.put('R', 1);
        LETTER_POINTS.put('S', 1); LETTER_POINTS.put('T', 1); LETTER_POINTS.put('U', 1);
        LETTER_POINTS.put('V', 4); LETTER_POINTS.put('W', 4); LETTER_POINTS.put('X', 8);
        LETTER_POINTS.put('Y', 4); LETTER_POINTS.put('Z', 10); LETTER_POINTS.put('-', 0);
    }
    //REQUIRES: Tile Bag is empty
    //MODIFIES: this
    //EFFECTS: Adds correct number of each letter
    // and blank tile to the tile bag.
    private void initializeTiles() {
        for (Map.Entry<Character, Integer> entry : LETTER_FREQUENCIES.entrySet()) {
            char letter = entry.getKey();
            int frequency = entry.getValue();
            // get points from Point map
            int points = LETTER_POINTS.get(letter); 
            addTiles(letter,points,frequency);
        }
    }

    //MODIFIES: this
    //EFFECTS: Adds a new Letter Tile to the tile bag
    //          the specified number of times, for
    //  example when a player wants to trade in tiles 
    //  and skip their turn
    private void addTiles(char letter, int points, int numberToAdd) {
        for (int i = 0; i < numberToAdd; i++) {
            drawPile.add(new LetterTile(letter,points));
        }
    }

    //MODIFIES: this
    //EFFECTS: Removes random tile from tile bag and returns it,
    //            null if empty
    public LetterTile drawTile() {
        if (drawPile.size() == 0) {
            return null;
        }
        int nextTileIndex = randomIndexGenerator.nextInt(this.numTilesRemaining());
        LetterTile nextTile = drawPile.get(nextTileIndex);
        drawPile.remove(nextTile);
        return nextTile;
    }
    // EFFECTS: Number of tiles left in tile bag
    public int numTilesRemaining() {
        return drawPile.size();
    }
}
