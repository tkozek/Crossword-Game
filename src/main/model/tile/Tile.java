package model.tile;

// Represents any tile in a game of Scrabble
public interface Tile {

    String toDisplay();

    String getTerminalPrintoutString();

    boolean occupiesBoardSpot();

    int getPoints();
}
