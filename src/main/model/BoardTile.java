package model;

//Represents a space on the Scrabble board which
// has not been played on yet. May have special properties
// such as Triple Word, Double Word, Triple Letter, Double Letter
// which impact scoring
public class BoardTile implements Tile {


    // Makes BoardTile with specified special tile properties,
    // at specified (row, column) index of this board
    
    public BoardTile(int row, int col, boolean doubleLetter, boolean doubleWord,
     boolean tripleLetter,boolean tripleWord) {
        
    }

    public boolean checkIsDoubleLetter() {
        return false;
    }

    public boolean checkIsDoubleWord() {
        return false;
    }

    public boolean checkIsTripleLetter() {
        return false;
    }

    public boolean checkIsTripleWord() {
        return false;
    }

    public int getRow() {
        return 0;
    }
    public int getCol() {
        return 0;
    }

}
