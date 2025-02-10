package model;

//Represents a space on the Scrabble board which
// has not been played on yet. May have special properties
// such as Triple Word, Double Word, Triple Letter, Double Letter
// which impact scoring
public class BoardTile implements Tile {

    private int row;
    private int col;
    private boolean doubleLetter;
    private boolean doubleWord;
    private boolean tripleLetter;
    private boolean tripleWord;

    // Makes BoardTile with specified special tile properties,
    // at specified (row, column) index of this board
    
    public BoardTile(int row, int col, boolean doubleLetter, boolean doubleWord,
     boolean tripleLetter,boolean tripleWord) {
        this.row = row;
        this.col = col;
        this.doubleLetter = doubleLetter;
        this.doubleWord = doubleWord;
        this.tripleLetter = tripleLetter;
        this.tripleWord = tripleWord;
    }

    public boolean checkIsDoubleLetter() {
        return this.doubleLetter;
    }

    public boolean checkIsDoubleWord() {
        return this.doubleWord;
    }

    public boolean checkIsTripleLetter() {
        return this.tripleLetter;
    }

    public boolean checkIsTripleWord() {
        return this.tripleWord ;
    }

    public int getRow() {
        return this.row;
    }
    public int getCol() {
        return this.col;
    }

}
