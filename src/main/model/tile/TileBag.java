package model.tile;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;

import model.Player;
import persistance.Writable;

import java.util.HashMap;

// Represents the draw bag of Letter Tiles in 
// a Scrabble Game
public class TileBag implements Writable {
    public static final int TOTAL_LETTERS_INITIALLY = 100;
    public static final int MAX_NUM_PLAYER_TILES = 7;
    private Map<Character, Integer> letterFrequencyMap = new HashMap<>();
    private List<LetterTile> drawPile;
    private Random randomIndexGenerator;
    

    //Initializes tile bag with number of each 
    //letter standard to a Scrabble Game
    public TileBag() {
        drawPile = new ArrayList<>();
        initializeMaps();
        initializeTiles(letterFrequencyMap);
        randomIndexGenerator = new Random();
    }
    
    // MODIFIES: this
    // EFFECTS: Puts appropriate frequencies 
    // and points values in point and frequency 
    // maps, for each valid character in Scrabble
    private void initializeMaps() {
        initializeFrequenciesAThroughM(letterFrequencyMap);
        initializeFrequenciesNThroughBlank(letterFrequencyMap);
    }

    // MODIFIES: this
    // EFFECTS: Adds Tile Frequencies to 
    //   Frequency map for Letters A, B, C,....,M
    //     inclusive
    private void initializeFrequenciesAThroughM(Map<Character, Integer> letterFrequencyMap) {
        letterFrequencyMap.put('A', 9); 
        letterFrequencyMap.put('B', 2); 
        letterFrequencyMap.put('C', 2);
        letterFrequencyMap.put('D', 4); 
        letterFrequencyMap.put('E', 12); 
        letterFrequencyMap.put('F', 2);
        letterFrequencyMap.put('G', 3); 
        letterFrequencyMap.put('H', 2); 
        letterFrequencyMap.put('I', 9);
        letterFrequencyMap.put('J', 1); 
        letterFrequencyMap.put('K', 1); 
        letterFrequencyMap.put('L', 4);
        letterFrequencyMap.put('M', 2); 
    }
    
    // MODIFIES: this
    // EFFECTS: Adds Tile Frequencies to 
    //   Frequency map for Letters N, O,....,Z,_,
    //     inclusive
    private void initializeFrequenciesNThroughBlank(Map<Character, Integer> letterFrequencyMap) {
        letterFrequencyMap.put('N', 6); 
        letterFrequencyMap.put('O', 8);
        letterFrequencyMap.put('P', 2); 
        letterFrequencyMap.put('Q', 1); 
        letterFrequencyMap.put('R', 6);
        letterFrequencyMap.put('S', 4); 
        letterFrequencyMap.put('T', 6); 
        letterFrequencyMap.put('U', 4);
        letterFrequencyMap.put('V', 2); 
        letterFrequencyMap.put('W', 2); 
        letterFrequencyMap.put('X', 1);
        letterFrequencyMap.put('Y', 2); 
        letterFrequencyMap.put('Z', 1); 
        letterFrequencyMap.put('-', 2);
    }

    //REQUIRES: Tile Bag is empty
    //MODIFIES: this
    //EFFECTS: Adds correct number of each letter
    // and blank tile to the tile bag.
    public void initializeTiles(Map<Character, Integer> letterFrequencyMap) {
        for (Map.Entry<Character, Integer> entry : letterFrequencyMap.entrySet()) {
            char letter = entry.getKey();
            int frequency = entry.getValue();
            // get points from Point map
            addTiles(letter, frequency);
        }
    }

    //MODIFIES: this
    //EFFECTS: Adds a new Letter Tile to the tile bag
    //  the specified number of times.
    private void addTiles(char letter, int numberToAdd) {
        for (int i = 0; i < numberToAdd; i++) {
            drawPile.add(new LetterTile(letter));
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds already existing tiles back to draw pile
    public void addTiles(List<LetterTile> lettersToAdd) {
        for (LetterTile letter : lettersToAdd) {
            drawPile.add(letter);
        }
    }

    //MODIFIES: this, player
    //EFFECTS: Removes LetterTiles from the tile bag until
    //     player's tile rack has Player.MAX_NUM_TILES tiles, 
    //     or the tile bag
    //     is empty. Returns number of tiles drawn.
    //     OR
    //     Returns -1 if draw pile is empty before removing any tiles
    public int drawTiles(Player player) {
        if (drawPile.size() == 0) {
            return -1;
        }
        int numTilesAdded = 0;
        int nextTileIndex;
        LetterTile nextTile;
        while (player.getNumTilesOnRack() < MAX_NUM_PLAYER_TILES && !drawPile.isEmpty()) {
            nextTileIndex = randomIndexGenerator.nextInt(this.numTilesRemaining());
            nextTile = drawPile.get(nextTileIndex);
            player.addTile(nextTile);
            drawPile.remove(nextTile);
            numTilesAdded++;
        }
        return numTilesAdded;
    }

    // EFFECTS: returns number of tiles left in tile bag
    public int numTilesRemaining() {
        return drawPile.size();
    }

    // MODIFIES: this
    // EFFECTS: Empties the draw pile so there are
    // no letter tiles in it
    public void emptyDrawPile() {
        drawPile.clear();
    }

    // EFFECTS: returns true if the tile bag
    //  has the specific tile object
    //   (for test purposes)
    public boolean contains(LetterTile tile) {
        return drawPile.contains(tile);
    }
    
    // EFFECTS: adds tile to draw pile for testing
    //   purposes
    public void addTile(LetterTile tileToAdd) {
        drawPile.add(tileToAdd);
    }

    // EFFECTS: returns each game character mapped to how many 
    // occurences it has in the draw pile of a new game
    public Map<Character, Integer> getInitialLetterFrequencies() {
        Map<Character, Integer> copy = new HashMap<>();
        initializeFrequenciesAThroughM(copy);
        initializeFrequenciesNThroughBlank(copy);
        return (Map<Character,Integer>) copy;
    }

    // EFFECTS: returns each game character mapped to how many 
    // occurences it has remaining in the draw pile
    public Map<Character, Integer> getCurrentLetterFrequencies() {
        Map<Character,Integer> curCounts = new HashMap<>();
        for (LetterTile letter : drawPile) {
            char character = letter.getCharacter();
            curCounts.put(character, curCounts.getOrDefault(character,0) + 1);
        }
        return curCounts;
    }


    @Override
    // EFFECTS: returns the string corresponding to each remaining
    // tile in the draw pile and its remaining frequency as a key value
    // pair in the form "character" : frequency
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        Map<Character, Integer> currentLetterFrequencies = getCurrentLetterFrequencies();
        for (Map.Entry<Character, Integer> entry : currentLetterFrequencies.entrySet()) {
            json.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return json;
    }
}  
