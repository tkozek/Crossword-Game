package model;

import java.util.Objects;

import org.json.JSONObject;

import persistance.Writable;

// Represents a (row,column) coordinate on a board
public class Coordinate implements Writable {
    private int row;
    private int column;

    // REQUIRES: Both row and column are >= 0 and 
    // <= Board.BOARD_LENGTH
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    @Override
    // REQUIRES: coord is of type Coordinate.
    // EFFECTS: returns true if 
    // this.getRow() == coord.getRow()
    // AND this.getColumn() == coord.getColumn()
    public boolean equals(Object coord) {
        Coordinate toCompareCoord = (Coordinate) coord;
        return row == toCompareCoord.row && column == toCompareCoord.column;
          
    }
    
    @Override
    // EFFECTS: hashes the coordinate object
    // based on row and column values
    // so that HashSet can be used to initialize 
    // the board's special tiles
    public int hashCode() {
        return Objects.hash(row,column);
    }

    @Override
    public JSONObject toJSON() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJSON'");
    }
    
}