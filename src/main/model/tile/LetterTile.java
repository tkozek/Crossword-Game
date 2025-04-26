package model.tile;

import org.json.JSONObject;

import persistance.JsonWritable;

//Represents the playable tiles in a Scrabble Game

public class LetterTile implements Tile, JsonWritable<JSONObject> {

    private char character;
    private int points;

    //Creates a new tile of specified letter and point value
    public LetterTile(char character, int points) {
        this.character = character;
        this.points = points;
    }
    

    // EFFECTS: Creates deep copy of letter
    public LetterTile(LetterTile letter) {
        this.character = letter.getCharacter();
        this.points = letter.getPoints();
    }

    public LetterTile(char character) {
        this.character = character;
        this.points = TileBag.getLetterPoints(this.character);
    }

    public LetterTile(String character) {
        this.character = character.charAt(0);
        this.points = TileBag.getLetterPoints(this.character);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put(String.valueOf(getCharacter()), getPoints());
        return json;
    }

    @Override
    public boolean occupiesBoardSpot() {
        return true;
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
    public int getPoints() {
        return this.points;
    }

    public char getCharacter() {
        return this.character;
    }
}
