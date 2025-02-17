package model;

//Represents the playable tiles in a Scrabble Game

public class LetterTile implements Tile {

    private char character;
    private int points;

    //Creates a new tile of specified letter and point value
    public LetterTile(char character, int points) {
        this.character = character;
        this.points = points;
    }


    public char getCharacter() {
        return this.character;
    }
    
    public int getLetterPoints() {
        return this.points;
    }

    
    //EFFECTS: casts tile's character as a String type for display
    @Override
    public String getStringToDisplay() {
        return String.valueOf(this.character);
    }
}
