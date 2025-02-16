package model;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

// Represents a Scrabble Game Board
public class Board {
    public static final int BOARD_LENGTH = 15;
    private Set<Coordinate> doubleLetterCoordinates = new HashSet<>();
    private Set<Coordinate> doubleWordCoordinates = new HashSet<>();
    private Set<Coordinate> tripleLetterCoordinates = new HashSet<>();
    private Set<Coordinate> tripleWordCoordinates = new HashSet<>();
    private List<Tile> boardTiles;

    // Initializes a new Board, with standard
    // placement of Double word, Triple Word,
    // Double Letter, Triple Letter Tiles

    public Board() {

    }

    // MODIFIES: this
    // EFFECTS: Places special tiles at appropriate positions on board
    private void initializeBoard() {

    }

    // MODIFIES: this
    // EFFECTS: Places double letter squares at appropriate Coordinates on board
    private void initializeDoubleLetterSquares() {

    }

    // MODIFIES: this
    // EFFECTS: Places triple word squares at appropriate Coordinates on board
    private void initializeTripleLetterSquares() {

    }

    // MODIFIES: this
    // EFFECTS: Places double word squares at appropriate Coordinates on board
    private void initializeDoubleWordSquares() {

    }

    // MODIFIES: this
    // EFFECTS: Places triple word squares at appropriate Coordinates on board
    private void initializeTripleWordSquares() {

    }

    // EFFECTS: Returns true if all letters will fit within the boundaries
    // of the board, starting at [startRow][startCol] and proceeding in direction
    // until all letters are placed
    public boolean inBounds(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        return false;
    }

    // REQUIRES: inBounds(letters,startRow,startCol, direction) is true
    // EFFECTS: returns true if and only if 
    // all tiles in the desired section are BoardTiles
    public boolean sectionIsAvailable(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        return false;
    }

    // REQUIRES: 0 <= row < BOARD_HEIGHT and 0 <= col < BOARD_WIDTH
    // EFFECTS: returns true if board[row][col] is a BoardTile
    //    returns false if board[row][col] is a LetterTile
    public boolean squareIsAvailable(int row, int col) {
        return false;
    }

    // EFFECTS: returns true if inBounds()
    // AND section is sectionIsAvailable()
    public boolean canPlay() {
        return false;
    }

    //REQUIRES: canPlay() is true for given arguments
    //MODIFIES: this
    //EFFECTS: Places selected letters on board beginning at start position
    //         and going in specified direction. 
    //         returns points earned from the word
    public int playWord(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        return 0;
    }

    //REQUIRES: sectionIsAvailable() is true with same arguments
    //EFFECTS:  returns true if board[row][col] contains a board tile of given tile type
    public boolean sectionContainsTileType(List<LetterTile> letters, int startRow, 
                int startCol, Direction dir, TileType tileType) {
        return false;
    }

    //REQUIRES: squareIsAvailable() is true with same arguments
    //EFFECTS: returns true if board[row][col] contains a board tile of given tile type
    public boolean squareisTileType(int row, int col, TileType tileType) {
        return false;
    }

    
}
