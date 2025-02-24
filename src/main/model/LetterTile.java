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

    // EFFECTS: Creates deep copy of 
    // letter
    public LetterTile(LetterTile letter) {
        this.character = letter.getCharacter();
        this.points = letter.getLetterPoints();
    }

    public char getCharacter() {
        return this.character;
    }
    
    public int getLetterPoints() {
        return this.points;
    }

}
