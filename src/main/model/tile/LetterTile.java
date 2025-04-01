package model.tile;

import org.json.JSONObject;

import model.Utility;
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
        this.points = letter.getPoints();
    }

    public LetterTile(char character) {
        this.character = character;
        this.points = Utility.getLetterPoints(character);
    }

    public LetterTile(String character) {
        this.character = character.charAt(0);
        this.points = Utility.getLetterPoints(this.character);
    }
    
    public char getCharacter() {
        return this.character;
    }
    
    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(String.valueOf(getCharacter()), getPoints());
        return json;
    }

    @Override
    public String toDisplay() {
        return String.valueOf(character);
    }

    @Override
    public String getTerminalPrintoutString() {
        return " " + toDisplay() + "_| ";
    }

    @Override
    public boolean occupiesBoardSpot() {
        return true;
    }
}
