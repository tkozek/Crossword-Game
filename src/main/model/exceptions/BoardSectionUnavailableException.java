package model.exceptions;

import model.Direction;

public class BoardSectionUnavailableException extends Exception {

    public BoardSectionUnavailableException(int row, int col, Direction dir) {
        super("Player's selected tiles cannot be played starting at (" + row + ", " 
                + col + ") and moving in direction: " + dir);
    }
}
