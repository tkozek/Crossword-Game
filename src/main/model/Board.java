package model;

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
    // REQUIRES: 0 <= row < BOARD_HEIGHT AND 0 <= col < BOARD_WIDTH
    // EFFECTS: returns true if and only if 
    // all tiles in the desired section are BoardTiles
    public boolean sectionIsAvailable() {
        return false;
    }

    // REQUIRES: 0 <= row < BOARD_HEIGHT and 0 <= col < BOARD_WIDTH
    // EFFECTS: returns true if board[row][col] is a BoardTile
    //    returns false if board[row][col] is a LetterTile
    public boolean squareIsAvailable(int row, int col) {
        return false;
    }


}
