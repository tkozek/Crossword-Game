package model.tile;

// Represents any tile in a game of Scrabble
public interface Tile {

    boolean occupiesBoardSpot();

    String toDisplay();

    String getTerminalPrintoutString();

    int getPoints();
}
