package model.board;

import java.util.Objects;

// Represents a (row,column) coordinate on a board
public class Coordinate {
    private int row;
    private int column;

    // REQUIRES: Both row and column are >= 0 and 
    // <= Board.BOARD_LENGTH
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    
    // EFFECTS: returns true if input has the same
    //  row and column coordinate as this
    @Override
    public boolean equals(Object coord) {
        if (this == coord) {
            return true;
        }
        if (coord == null || getClass() != coord.getClass()) {
            return false;
        }
        Coordinate toCompareCoord = (Coordinate) coord;
        return row == toCompareCoord.row && column == toCompareCoord.column;
          
    }
    
    // EFFECTS: hashes the coordinate object
    // based on row and column values
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.column;
    }
}