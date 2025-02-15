package model;
import java.util.Objects;

// Represents a (row,column) coordinate on a board
public class Coordinate {
    
    // Creates a new coordinate with row, and column
    // REQUIRES: Both row and column are >= 0 and 
    // <= Board.BOARD_LENGTH

    public Coordinate() {

    }

    public int getRow() {
        return 0;
    }
    public int getColumn() {
        return 0;
    }

    @Override
    //EFFECTS: returns true if 
    // this.getRow() == coord.getRow()
    // AND this.getColumn() == coord.getColumn()
    public boolean equals(Object coord) {
        return false;
    }
    @Override
    // EFFECTS: hashes the coordinate object
    // based on row and column values
    // so that HashSet can be used to initialize 
    // the board's special tiles
    public int hashCode() {
        return 0;
    }
    
}
