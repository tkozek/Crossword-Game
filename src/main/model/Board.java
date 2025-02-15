package model;

import java.util.List;
import java.util.ArrayList;

// Represents a Scrabble Game Board
public class Board {

    // Initializes a new Board, with standard
    // placement of Double word, Triple Word,
    // Double Letter, Triple Letter Tiles

    public Board() {

    }

    // MODIFIES: this
    // EFFECTS: Places special tiles at appropriate positions on board
    private void initializeBoard() {

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
    //REQUIRES: inBounds() && sectionIsAvailable() for given arguments
    //MODIFIES: this
    //EFFECTS: Places selected letters on board beginning at start position
    //         and going in specified direction. 
    //         returns points earned from the word
    public int playWord(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        return 0;
    }

}
