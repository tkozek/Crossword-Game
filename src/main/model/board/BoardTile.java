package model.board;

import model.tile.Tile;
import model.tile.TileType;

// Represents a space on the Scrabble board which
// has not been played on yet. May have special properties
// such as Triple Word, Double Word, Triple Letter, Double Letter
// which impact scoring
public class BoardTile implements Tile {

    private int row;
    private int col;
    private TileType tileType;

    // Makes BoardTile with specified special tile properties,
    // at specified (row, column) index of this board        
    public BoardTile(int row, int col, TileType tileType) {
        this.row = row;
        this.col = col;
        this.tileType = tileType;
    }

    // EFFECTS: returns true if and only if this has 
    //   the desired tileType
    public boolean checkIsTileType(TileType tileType) {
        return this.tileType.equals(tileType);
    }


    public int getRow() {
        return this.row;
    }
    
    public int getCol() {
        return this.col;
    }

    public TileType getTileType() {
        return this.tileType;
    }

    // EFFECTS: returns string which 
    // represents the tile based on 
    // its point modifier
    public String toDisplay() {
        switch (tileType) {
            case DOUBLE_LETTER:
                return "DLS";
            case DOUBLE_WORD:
                return "DWS";
            case TRIPLE_LETTER:
                return "TLS";
            case TRIPLE_WORD:
                return "TWS";
            default:
                return " ";
        }
    }

}
