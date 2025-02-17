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
    private Tile[][] boardTiles;

    // Initializes a new Board, with standard
    // placement of Double word, Triple Word,
    // Double Letter, Triple Letter Tiles

    public Board() {
        doubleLetterCoordinates.add(new Coordinate(0,3));
        boardTiles = new Tile[BOARD_LENGTH][BOARD_LENGTH];
        initializeBoard();

    }

    // MODIFIES: this
    // EFFECTS: Places special tiles at appropriate positions on board
    private void initializeBoard() {
        initializeDoubleLetterSquares();
        initializeDoubleWordSquares();
        initializeTripleLetterSquares();
        initializeTripleWordSquares();
        fillBoardTiles();
    }

    // MODIFIES: this
    // EFFECTS: populates the board with Board Tiles
    //         assigns special squares at appropriate 
    //          coordinates
    private void fillBoardTiles() {
        TileType tileType;
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                Coordinate coord = new Coordinate(i,j);
                if (doubleLetterCoordinates.contains(coord)) {
                    tileType = TileType.DOUBLE_LETTER;
                } else if (doubleWordCoordinates.contains(coord)) {
                    tileType = TileType.DOUBLE_WORD;
                } else if (tripleLetterCoordinates.contains(coord)) {
                    tileType = TileType.TRIPLE_LETTER;
                } else if (tripleWordCoordinates.contains(coord)) {
                    tileType = TileType.TRIPLE_WORD;
                } else {
                    tileType = TileType.NORMAL;
                }
                boardTiles[i][j] = new BoardTile(i,j,tileType);
            }
        }
    }
    
    // EFFECTS: Indicates coordinates where a Double Letter Square
    //          should exist at the start of the game
    private void initializeDoubleLetterSquares() {
        doubleLetterCoordinates.add(new Coordinate(0, 11));
        doubleLetterCoordinates.add(new Coordinate(2, 6));
        doubleLetterCoordinates.add(new Coordinate(2, 8));
        doubleLetterCoordinates.add(new Coordinate(3, 0));
        doubleLetterCoordinates.add(new Coordinate(3, 7));
        doubleLetterCoordinates.add(new Coordinate(3, 14));
        doubleLetterCoordinates.add(new Coordinate(6, 2));
        doubleLetterCoordinates.add(new Coordinate(6, 6));
        doubleLetterCoordinates.add(new Coordinate(6, 8));
        doubleLetterCoordinates.add(new Coordinate(6, 12));
        doubleLetterCoordinates.add(new Coordinate(7, 3));
        doubleLetterCoordinates.add(new Coordinate(7, 11));
        doubleLetterCoordinates.add(new Coordinate(8, 2));
        doubleLetterCoordinates.add(new Coordinate(8, 6));
        doubleLetterCoordinates.add(new Coordinate(8, 8));
        doubleLetterCoordinates.add(new Coordinate(8, 12));
        doubleLetterCoordinates.add(new Coordinate(11, 0));
        doubleLetterCoordinates.add(new Coordinate(11, 7));
        doubleLetterCoordinates.add(new Coordinate(11, 14));
        doubleLetterCoordinates.add(new Coordinate(12, 6));
        doubleLetterCoordinates.add(new Coordinate(12, 8));
        doubleLetterCoordinates.add(new Coordinate(14, 3));
        doubleLetterCoordinates.add(new Coordinate(14, 11));
    }

    // EFFECTS: Indicates coordinates where a Triple Letter Square
    //          should exist at the start of the game
    private void initializeTripleLetterSquares() {
        tripleLetterCoordinates.add(new Coordinate(1, 5));
        tripleLetterCoordinates.add(new Coordinate(1, 9));
        tripleLetterCoordinates.add(new Coordinate(5, 1));
        tripleLetterCoordinates.add(new Coordinate(5, 5));
        tripleLetterCoordinates.add(new Coordinate(5, 9));
        tripleLetterCoordinates.add(new Coordinate(5, 13));
        tripleLetterCoordinates.add(new Coordinate(9, 1));
        tripleLetterCoordinates.add(new Coordinate(9, 5));
        tripleLetterCoordinates.add(new Coordinate(9, 9));
        tripleLetterCoordinates.add(new Coordinate(9, 13));
        tripleLetterCoordinates.add(new Coordinate(13, 5));
        tripleLetterCoordinates.add(new Coordinate(13, 9));
    }

    // EFFECTS: Indicates coordinates where a Double Word Square
    //          should exist at the start of the game
    private void initializeDoubleWordSquares() {
        doubleWordCoordinates.add(new Coordinate(1, 1));
        doubleWordCoordinates.add(new Coordinate(1, 13));
        doubleWordCoordinates.add(new Coordinate(2, 2));
        doubleWordCoordinates.add(new Coordinate(2, 12));
        doubleWordCoordinates.add(new Coordinate(3, 3));
        doubleWordCoordinates.add(new Coordinate(3, 11));
        doubleWordCoordinates.add(new Coordinate(4, 4));
        doubleWordCoordinates.add(new Coordinate(4, 10));
        doubleWordCoordinates.add(new Coordinate(10, 4));
        doubleWordCoordinates.add(new Coordinate(10, 10));
        doubleWordCoordinates.add(new Coordinate(11, 3));
        doubleWordCoordinates.add(new Coordinate(11, 11));
        doubleWordCoordinates.add(new Coordinate(12, 2));
        doubleWordCoordinates.add(new Coordinate(12, 12));
        doubleWordCoordinates.add(new Coordinate(13, 1));
        doubleWordCoordinates.add(new Coordinate(13, 13));
    }

    // EFFECTS: Indicates coordinates where a Triple Word Square
    //          should exist at the start of the game
    private void initializeTripleWordSquares() {
        tripleWordCoordinates.add(new Coordinate(0, 0));
        tripleWordCoordinates.add(new Coordinate(0, 7));
        tripleWordCoordinates.add(new Coordinate(0, 14));
        tripleWordCoordinates.add(new Coordinate(7, 0));
        tripleWordCoordinates.add(new Coordinate(7, 14));
        tripleWordCoordinates.add(new Coordinate(14, 0));
        tripleWordCoordinates.add(new Coordinate(14, 7));
        tripleWordCoordinates.add(new Coordinate(14, 14));
    }

    // EFFECTS: Returns true if all letters will fit within the boundaries
    // of the board, starting at [startRow][startCol] and proceeding in direction
    // until all letters are placed
    public boolean inBounds(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        
        int length = letters.size();
        if (startRow < 0 || startCol < 0) {
            return false;
        }
        if (dir == Direction.RIGHT) {
            if (startCol + length > BOARD_LENGTH) {
                return false;
            }
        } else if (startRow + length > BOARD_LENGTH) {
            return false;
            
        }
        return true;
    }

    // REQUIRES: inBounds(letters,startRow,startCol, direction) is true
    // EFFECTS: returns true if and only if 
    // all tiles in the desired section are BoardTiles
    public boolean sectionIsAvailable(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        int length = letters.size();
        if (dir == Direction.RIGHT) {
            for (int col = startCol; col < startCol + length; col++) {
                if (!(boardTiles[startRow][col] instanceof BoardTile)) {
                    return false;
                }
            }
        } else {
            for (int row = startRow; row < startRow + length; row++) {
                if (!(boardTiles[row][startCol] instanceof BoardTile)) {
                    return false;
                }
            }
        } 
        return true;
    }

    // EFFECTS: returns true if inBounds()
    // AND section is sectionIsAvailable()
    public boolean canPlay(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        boolean inBounds =  inBounds(letters, startRow, startCol, dir);
        if (!inBounds) {
            return false;
        }
        boolean sectionAvailable = sectionIsAvailable(letters, startRow, startCol, dir);
        return sectionAvailable;
    }

    //REQUIRES: canPlay() is true for given arguments
    //MODIFIES: this
    //EFFECTS: Places selected letters on board beginning at start position
    //         and going in specified direction. 
    //         returns points earned from the word
    public int playWord(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        int score = scoreWord(letters, startRow, startCol, dir);
        placeWord(letters, startRow, startCol, dir);
        return score;
    }

    public int scoreWord(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        int wordMultiplier = 1;
        int total = 0;
        int rowInc = (dir == Direction.DOWN) ? 1 : 0;
        int colInc = (dir == Direction.RIGHT) ? 1 : 0;
        for (int i = 0; i < letters.size(); i++) {
            int letterPoints = letters.get(i).getLetterPoints();
            Coordinate coord = new Coordinate(startRow + i * rowInc, startCol + i * colInc);
            if (doubleLetterCoordinates.contains(coord)) {
                letterPoints *= 2;
                doubleLetterCoordinates.remove(coord);
            } else if (doubleWordCoordinates.contains(coord)) {
                wordMultiplier *= 2;
                doubleWordCoordinates.remove(coord);
            } else if (tripleLetterCoordinates.contains(coord)) {
                letterPoints *= 3;
                tripleLetterCoordinates.remove(coord);
            } else if (tripleWordCoordinates.contains(coord)) {
                wordMultiplier = 3;
                tripleWordCoordinates.remove(coord);
            }
            total += letterPoints;
        }
        return total * wordMultiplier;
    }

    public void placeWord(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        int length = letters.size();
        int rowIncrement = (dir == Direction.DOWN) ? 1 : 0;
        int colIncrement = (dir == Direction.RIGHT) ? 1 : 0;
        for (int i = 0; i < length; i++) {
            int row = startRow + i * rowIncrement;
            int col = startCol + i * colIncrement;
            boardTiles[row][col] = letters.get(i);
        }
    }

   /*  //REQUIRES: sectionIsAvailable() is true with same arguments
    //EFFECTS:  returns true if board[row][col] contains a board tile of given tile type
    public boolean sectionContainsTileType(List<LetterTile> letters, int startRow, 
                int startCol, Direction dir, TileType tileType) {
        int length = letters.size();
        int rowIncrement = (dir == Direction.DOWN) ? 1 : 0;
        int colIncrement = (dir == Direction.RIGHT) ? 1 : 0;
        for (int i = 0; i < length; i++) {
            int row = startRow + i * rowIncrement;
            int col = startCol + i * colIncrement;
            Coordinate coord = new Coordinate(row, col);
            if (tileType == TileType.DOUBLE_LETTER && doubleLetterCoordinates.contains(coord) 
                    || tileType == TileType.DOUBLE_WORD && doubleWordCoordinates.contains(coord) 
                    || tileType == TileType.TRIPLE_LETTER && tripleLetterCoordinates.contains(coord) 
                    || tileType == TileType.TRIPLE_WORD && tripleWordCoordinates.contains(coord)) {
                return true;
            }
        }
        return false;
    } */

    /* //REQUIRES: squareIsAvailable() is true with same arguments
    //EFFECTS: returns true if board[row][col] contains a board tile of given tile type
    public boolean squareisTileType(int row, int col, TileType tileType) {
        Coordinate coord = new Coordinate(row,col);
        if (tileType == TileType.DOUBLE_LETTER) {
            return doubleLetterCoordinates.contains(coord);
        } else if (tileType == TileType.DOUBLE_WORD) {
            return doubleWordCoordinates.contains(coord);
        } else if (tileType == TileType.TRIPLE_LETTER) {
            return tripleLetterCoordinates.contains(coord);
        } else if (tileType == TileType.TRIPLE_WORD) {
            return tripleWordCoordinates.contains(coord);
        } else {
            return false;
        }
    } */

    
}
