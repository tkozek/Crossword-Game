package model.tile;

import org.json.JSONObject;

import persistance.Writable;

//Represents the playable tiles in a Scrabble Game

public class LetterTile implements Tile, Writable {

    private char character;
    private int points;

    //Creates a new tile of specified letter and point value
    public LetterTile(char character, int points) {
        this.character = character;
        this.points = points;
    }

    // EFFECTS: Creates deep copy of 
    // letter
    public LetterTile(LetterTile letter) {
        this.character = letter.getCharacter();
        this.points = letter.getLetterPoints();
    }

    public LetterTile(char character) {
        this.character = character;
        this.points = TileBag.getPointsForLetter(character);
    }
    

    public char getCharacter() {
        return this.character;
    }
    
    public int getLetterPoints() {
        return this.points;
    }

    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJSON'");
    }

}
