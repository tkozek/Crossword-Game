package model;

//Represents the playable tiles in a Scrabble Game

public class LetterTile implements Tile {

    //Creates a new tile of specified letter and point value
    public LetterTile(char character, int points) {
        
    }


    public char getCharacter() {
        return '_';
    }
    public int getLetterPoints() {
        return 0;
    }

    //EFFECTS: casts tile's character as a String type
    public String getCharacterAsString() {
        return "";
    }
}
