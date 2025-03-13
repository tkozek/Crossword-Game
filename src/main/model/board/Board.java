package model.board;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;

import model.Direction;
import model.tile.LetterTile;
import model.tile.Tile;
import model.tile.TileType;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

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
    // assigns special squares at appropriate coordinates
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
    
    // MODIFIES: this
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

    // MODIFIES: this
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

    // MODIFIES: this
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
        doubleWordCoordinates.add(new Coordinate(7, 7));
        doubleWordCoordinates.add(new Coordinate(10, 4));
        doubleWordCoordinates.add(new Coordinate(10, 10));
        doubleWordCoordinates.add(new Coordinate(11, 3));
        doubleWordCoordinates.add(new Coordinate(11, 11));
        doubleWordCoordinates.add(new Coordinate(12, 2));
        doubleWordCoordinates.add(new Coordinate(12, 12));
        doubleWordCoordinates.add(new Coordinate(13, 1));
        doubleWordCoordinates.add(new Coordinate(13, 13));
    }

    // MODIFIES: this
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

    // EFFECTS: returns true if and only if all letters can be placed without going
    // out of bounds, skips over spaces where a letter is already played
    // returns false if startRow,startCol is out of bounds.
    public boolean sectionIsAvailable(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        int length = letters.size();
        if (startRow < 0 || startCol < 0 || boardTiles[startRow][startCol] instanceof LetterTile) {
            return false;
        }
        int rowInc = (dir == Direction.DOWN) ? 1 : 0;
        int colInc = (dir == Direction.RIGHT) ? 1 : 0;
        int i = 0;
        while (i < length) {
            if (startRow + rowInc * i >= BOARD_LENGTH || startCol + colInc * i >= BOARD_LENGTH) {
                return false;
            }
            if (!(boardTiles[startRow + rowInc * i][startCol + colInc * i] instanceof BoardTile)) {
                length++;
            }
            i++;
        }
        return true;
    }

    //REQUIRES: sectionIsAvailable() is true for given arguments
    //MODIFIES: this
    //EFFECTS: Places selected letters on board beginning at start position
    //         and going in specified direction. 
    //         returns points earned from the word
    public int playWord(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        int rowInc = (dir == Direction.DOWN) ? 1 : 0;
        int colInc = (dir == Direction.RIGHT) ? 1 : 0;
        int score = scoreWord(letters, startRow, startCol, rowInc, colInc);
        placeWord(letters, startRow, startCol, dir);
        return score;
    }


  /*   // REQUIRES: sectionIsAvailable() is true for given arguments
    // EFFECTS: Returns the score earned for playing letters in the given direction
    // beginning at starting coordinates
    public int scoreWord(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        int wordMultiplier = 1;
        int total = 0;
        int rowInc = (dir == Direction.DOWN) ? 1 : 0;
        int colInc = (dir == Direction.RIGHT) ? 1 : 0;
        for (int i = 0; i < letters.size(); i++) {
            int letterPoints = letters.get(i).getLetterPoints();
            Coordinate coord = new Coordinate(startRow + i * rowInc, startCol + i * colInc);
            int tempWordMultiplier = findWordMultiplier(coord);
            int tempLetterMultiplier = findLetterMultiplier(coord);
            letterPoints *= tempLetterMultiplier;
            wordMultiplier *= tempWordMultiplier;
            total += letterPoints;
        }
        return total * wordMultiplier;
    } */

      // REQUIRES: sectionIsAvailable() is true for given arguments
    // EFFECTS: Returns the score earned for playing letters in the given direction
    // beginning at starting coordinates
    public int scoreWord(List<LetterTile> letters, int startRow, int startCol, int rowInc, int colInc) {
        int wordMultiplier = 1;
        int total = 0;
        int adjTotal = 0;
        int numPlaced = 0;
        int i = 0;
        while (numPlaced < letters.size()) {
            if (boardTiles[startRow + i * rowInc][ startCol + i * colInc] instanceof BoardTile) {
                // we can place a letter here, and need recursive calls
                LetterTile letter = letters.get(numPlaced);
                Coordinate coord = new Coordinate(startRow + i * rowInc, startCol + i * colInc);
                adjTotal += scorePerpendicularWord(letter, coord.getRow(), coord.getCol(), colInc, rowInc);
                wordMultiplier *= findWordMultiplier(coord, false);
                numPlaced++;
                total += letter.getLetterPoints() * findLetterMultiplier(coord, false);
            } else {
                LetterTile alreadyPlacedLetter = (LetterTile) boardTiles[startRow + i * rowInc][startCol + i * colInc];
                total += alreadyPlacedLetter.getLetterPoints();
                // add that score but no recursive calls and no new letters placed
            }
            i++;
        }
        total += getInlineAdjacentPoints(startRow, startCol, rowInc, colInc, i);
        return total * wordMultiplier + adjTotal;
    }

    // REQUIRES: startRow and startCol are both in bounds, shift >= 0, exactly one of rowInc and colInc are 
    // 1 and 0 respectively.
    // EFFECTS: returns any points earned from existing tiles prior to first letter placed and after last letter
    // placed in this turn. 
    private int getInlineAdjacentPoints(int startRow, int startCol, int rowInc, int colInc, int shift) {
        int total = 0;
        // start at point after final placed tile
        while ((startRow + rowInc * shift) < BOARD_LENGTH && (startCol + colInc * shift) < BOARD_LENGTH 
                && boardTiles[startRow + rowInc * shift][startCol + colInc * shift] instanceof LetterTile) {
            LetterTile letter = (LetterTile) boardTiles[startRow + rowInc * shift][startCol + colInc * shift];
            total += letter.getLetterPoints();
            shift++;
        }
        shift = -1;
        // then go back starting from one space before first tile
        while ((startRow + rowInc * shift) >= 0 && (startCol + colInc * shift) >= 0 
            && boardTiles[startRow + rowInc * shift][startCol + colInc * shift] instanceof LetterTile) {
            LetterTile letter = (LetterTile) boardTiles[startRow + rowInc * shift][startCol + colInc * shift];
            total += letter.getLetterPoints();
            shift--;
        }
        return total;
    }

    // EFFECTS: Checks for multipliers at the starting coordinate, then
    // iteratively adds points from already placed in-line letter tiles
    // which connect to the starting coordinate. returns score
    // after applying relevant multiplier.
    private int scorePerpendicularWord(LetterTile letter, int startRow, int startCol, int rowInc, int colInc) {
        Coordinate coord = new Coordinate(startRow, startCol);
        int total = 0;
        int letterPoints = letter.getLetterPoints();
        boolean blankStarter = letterPoints == 0; // display may change, but value of blank is always zero
        total += scorePosAdjacent(startRow, startCol, rowInc, colInc);
        total += scoreNegAdjacent(startRow, startCol, rowInc, colInc);
        if (total == 0 && !blankStarter) {
            return 0;
        } else {
            return (total + letterPoints * findLetterMultiplier(coord, true)) * findWordMultiplier(coord, true);
        }
    }

    private int scorePosAdjacent(int startRow, int startCol, int rowInc, int colInc) {
        int i = 1;
        int total = 0;
        while (inBounds(startRow + rowInc * i, startCol + colInc * i)) {
            if (boardTiles[startRow + rowInc * i][startCol + colInc * i] instanceof LetterTile) {
                LetterTile letter = (LetterTile) boardTiles[startRow + rowInc * i][startCol + colInc * i];
                total += letter.getLetterPoints();
            } else {
                break;
            }
            i++;
        }
        return total;
    }

    private int scoreNegAdjacent(int startRow, int startCol, int rowInc, int colInc) {
        int i = -1;
        int total = 0;
        while (inBounds(startRow + rowInc * i, startCol + colInc * i)) {
            if (boardTiles[startRow + rowInc * i][startCol + colInc * i] instanceof LetterTile) {
                LetterTile letter = (LetterTile) boardTiles[startRow + rowInc * i][startCol + colInc * i];
                total += letter.getLetterPoints();
            } else {
                break;
            }
            i--;
        }
        return total;
    }

    public boolean inBounds(int row, int col) {
        return (row >= 0 && row < BOARD_LENGTH && col >= 0 && col < BOARD_LENGTH);
    }

    // MODIFIES: this
    // EFFECTS: returns the word multiplier for the
    // board space at coord, removes multiplier from 
    // that coordinate so that it isn't used a second time
    public int findWordMultiplier(Coordinate coord, boolean isAdjacency) {
        if (doubleWordCoordinates.contains(coord)) {
            if (!isAdjacency) {
                doubleWordCoordinates.remove(coord);
            }
            return 2;
        } else if (tripleWordCoordinates.contains(coord)) {
            if (!isAdjacency) {
                tripleWordCoordinates.remove(coord);
            }
            return 3;
        } else {
            return 1;
        }
    }

    // MODIFIES: this
    // EFFECTS: returns the letter multiplier for the
    // board space at coord, removes multiplier from 
    // that coordinate so that it isn't used a second time
    public int findLetterMultiplier(Coordinate coord, boolean isAdjacency) {
        if (doubleLetterCoordinates.contains(coord)) {
            if (!isAdjacency) {
                doubleLetterCoordinates.remove(coord);
            }
            return 2;
        } else if (tripleLetterCoordinates.contains(coord)) {
            if (!isAdjacency) {
                tripleLetterCoordinates.remove(coord);
            }
            return 3;
        } else {
            return 1;
        }
    }

    // REQUIRES: sectionIsAvailable() is true for given arguments
    // MODIFIES: this
    // EFFECTS: places letters onto board in the given direction
    // beginning at starting coordinates
    public void placeWord(List<LetterTile> letters, int startRow, int startCol, Direction dir) {
        int length = letters.size();
        int rowIncrement = (dir == Direction.DOWN) ? 1 : 0;
        int colIncrement = (dir == Direction.RIGHT) ? 1 : 0;
        int i = 0;
        int lettersPlaced = 0;
        while (lettersPlaced < length) {
            int row = startRow + i * rowIncrement;
            int col = startCol + i * colIncrement;
            if (!(boardTiles[row][col] instanceof LetterTile)) {
                boardTiles[row][col] = letters.get(lettersPlaced);
                lettersPlaced++;
            }
            i++;
        }
    }

    // MODIFIES: this
    // EFFECTS: updates board at given position to the given letter
    // removes position from any special scoring squares list
    // (since it was already used)
    public void updatePositionToMatchOldBoard(LetterTile letter, int row, int col) {
        boardTiles[row][col] = new LetterTile(letter);
        Coordinate coord = new Coordinate(row, col);
        doubleLetterCoordinates.remove(coord);
        doubleWordCoordinates.remove(coord);
        tripleLetterCoordinates.remove(coord);
        tripleWordCoordinates.remove(coord);
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


    //EFFECTS: returns Chacters 'A' to 'Z' and '_'
    //   mapped to their number of occurences on board
    public Map<Character, Integer> getNumEachCharOnBoard() {
        Map<Character, Integer> charCounts = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (boardTiles[i][j] instanceof BoardTile) {
                    continue;
                } else { //(boardTiles[i][j] instanceof LetterTile) {
                    LetterTile tile = (LetterTile) boardTiles[i][j];
                    char letterChar = tile.getCharacter();
                    charCounts.put(letterChar, charCounts.getOrDefault(letterChar,0) + 1);
                } 
            }
        }
        return charCounts;
    }

    public Tile getTileAtPositionOnBoard(int row, int column) {
        return boardTiles[row][column];
    }

    public JSONArray toJson() {
        JSONArray json = new JSONArray();
        for (int i = 0; i < BOARD_LENGTH; i++) {
            for (int j = 0; j < BOARD_LENGTH; j++) {
                if (boardTiles[i][j] instanceof LetterTile) {
                    LetterTile letterTile = (LetterTile) boardTiles[i][j];
                    json.put(String.valueOf(letterTile.getCharacter()));
                } else {
                    BoardTile boardTile = (BoardTile) boardTiles[i][j];
                    json.put(boardTile.toDisplay());
                }
            }
        }
        return json;
    }
}

