package model.board;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Direction;
import model.Player;
import model.ScrabbleGame;
import model.tile.LetterTile;
import model.tile.TileBag;
import model.tile.TileType;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class BoardTest {
    
    private Board board;
    private TileBag testBag;
    private Player testPlayer;
    private Board testBoard;
    private List<LetterTile>  letters;
    private LetterTile B;
    private LetterTile A;
    private LetterTile Z;
    private ScrabbleGame game;

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        game = new ScrabbleGame("alphabet", board, testBag);
        testPlayer = new Player("Trevor", testBoard, testBag, game);
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        B = new LetterTile('B',3);
        A = new LetterTile('A',1);
        Z = new LetterTile('Z',10);
    }
    
    @Test 

    void testInBounds() {

    }
    /* @Test
    void testSectionContainsWithAllTypes() {
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.DOWN, TileType.DOUBLE_LETTER));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.DOWN, TileType.TRIPLE_WORD));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.RIGHT, TileType.DOUBLE_WORD));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.RIGHT, TileType.TRIPLE_LETTER));
    } */

    /* @Test
    void testSquareIsTypeAllTypes() {
        assertTrue(board.squareisTileType(7,7,TileType.DOUBLE_WORD));
        assertFalse(board.squareisTileType(7,7,TileType.DOUBLE_LETTER));
        assertFalse(board.squareisTileType(7,7,TileType.TRIPLE_LETTER));
        assertFalse(board.squareisTileType(7,7,TileType.TRIPLE_WORD));
        //It's normal
        assertFalse(board.squareisTileType(4,5,TileType.DOUBLE_LETTER));
        assertTrue(board.squareisTileType(4,5,TileType.NORMAL));

        assertTrue(board.squareisTileType(5,13,TileType.TRIPLE_LETTER));
        assertFalse(board.squareisTileType(5,13,TileType.DOUBLE_LETTER));
        assertFalse(board.squareisTileType(5,13,TileType.DOUBLE_WORD));
        assertFalse(board.squareisTileType(5,13,TileType.TRIPLE_WORD));

        assertTrue(board.squareisTileType(14,14,TileType.TRIPLE_WORD));
        assertFalse(board.squareisTileType(14,14,TileType.DOUBLE_LETTER));
        assertFalse(board.squareisTileType(14,14,TileType.DOUBLE_WORD));
        assertFalse(board.squareisTileType(14,14,TileType.TRIPLE_LETTER));

    } */


   /*  @Test 
    public void testSquareIsAvailable() {
        assertTrue(board.squareIsAvailable(0, 0));
        assertTrue(board.squareIsAvailable(14, 14));
        assertTrue(board.squareIsAvailable(7, 7));
        assertTrue(board.squareIsAvailable(8, 1));
        assertTrue(board.squareIsAvailable(1, 13));

    } */

    @Test 
    public void testSectionIsAvailableNothingAddedGoingRight() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();

        boolean isAvailable = board.sectionIsAvailable(letters, 7,7,Direction.RIGHT);
        assertTrue(isAvailable);
    }

    @Test 
    public void testSectionIsAvailableAboveAndBelowWordGoingRight() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();

        boolean isAvailable = board.sectionIsAvailable(letters, 7,7,Direction.RIGHT);
        assertTrue(isAvailable);
        board.playWord(letters, 7, 7, Direction.RIGHT);
        isAvailable = board.sectionIsAvailable(letters, 7,7,Direction.RIGHT);
        assertFalse(isAvailable);

        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        boolean isAvailableBelow = board.sectionIsAvailable(letters, 8,7,Direction.RIGHT);
        assertTrue(isAvailableBelow);
        boolean isAvailableAbove = board.sectionIsAvailable(letters, 6,7,Direction.RIGHT);
        assertTrue(isAvailableAbove);
    }

    @Test
    void testInBoundsRight() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        assertEquals(7, letters.size());
        //
        Direction dir = Direction.RIGHT;
        assertTrue(board.inBounds(letters, 7,3, dir));
        assertTrue(board.inBounds(letters, 7,2, dir));
        assertTrue(board.inBounds(letters, 7,1, dir));

        assertTrue(board.inBounds(letters, 14,7, dir));
        assertTrue(board.inBounds(letters, 14,8, dir));
        assertFalse(board.inBounds(letters, 14,9, dir));
        assertFalse(board.inBounds(letters, 14,10, dir));

        assertTrue(board.inBounds(letters, 0,5, Direction.RIGHT));
        assertTrue(board.inBounds(letters, 0,6, Direction.RIGHT));
        assertTrue(board.inBounds(letters, 0,7, Direction.RIGHT));

    }

    @Test
    void testInBoundsDown() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        //
        assertTrue(board.inBounds(letters, 7,7, Direction.DOWN));
        assertTrue(board.inBounds(letters, 8,7, Direction.DOWN));
        assertFalse(board.inBounds(letters, 9,7, Direction.DOWN));

        assertTrue(board.inBounds(letters, 7,14, Direction.DOWN));
        assertTrue(board.inBounds(letters, 8,14, Direction.DOWN));
        assertFalse(board.inBounds(letters, 9,14, Direction.DOWN));

        assertTrue(board.inBounds(letters, 7,0, Direction.DOWN));
        assertTrue(board.inBounds(letters, 8,0, Direction.DOWN));
        assertFalse(board.inBounds(letters, 9,0, Direction.DOWN));

    }

    @Test
    void testInBoundsStartOOBMoveDownIntoBounds() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        //
        assertFalse(board.inBounds(letters, -1,0, Direction.DOWN));
        assertFalse(board.inBounds(letters, -1,14, Direction.DOWN));
        assertFalse(board.inBounds(letters, -2,7, Direction.DOWN));
    }

    @Test
    void testInBoundsStartOOBMoveRightUntilInBounds() {
        testBag.drawTiles(testPlayer);
        letters = testPlayer.getTilesOnRack();
        //
        assertFalse(board.inBounds(letters, 0,-2, Direction.RIGHT));
        assertFalse(board.inBounds(letters, 7,-1, Direction.RIGHT));
        assertFalse(board.inBounds(letters, 14,-1, Direction.RIGHT));
    }

    @Test
    void testSectionNotAvailableTileWasPlayed() {
        assertEquals(letters.size(),7);
        board.playWord(letters, 0,0,Direction.DOWN);
        assertFalse(board.sectionIsAvailable(letters, 0,0, Direction.DOWN));
        assertFalse(board.sectionIsAvailable(letters, 0,0, Direction.RIGHT));

    }

    @Test
    void testSectionNotStaggered() {
        assertEquals(letters.size(),7);
        board.playWord(letters, 4,4,Direction.DOWN);
        assertFalse(board.sectionIsAvailable(letters, 4,0, Direction.RIGHT));
        assertFalse(board.sectionIsAvailable(letters, 0,4, Direction.DOWN));

    }

    @Test
    void testSectionAvailableBarelyOverlap() {
        assertEquals(letters.size(),7);
        board.playWord(letters, 7,7,Direction.DOWN);
        assertFalse(board.sectionIsAvailable(letters, 7,1, Direction.RIGHT));
        assertFalse(board.sectionIsAvailable(letters, 1,7, Direction.DOWN));
        assertTrue(board.sectionIsAvailable(letters, 7,0, Direction.RIGHT));
        assertTrue(board.sectionIsAvailable(letters, 0,7, Direction.DOWN));
    }

    @Test 
    void testCanPlay() {
        assertTrue(board.canPlay(letters, 0, 0, Direction.DOWN));
        assertTrue(board.canPlay(letters, 0, 0, Direction.RIGHT));

        assertFalse(board.canPlay(letters, 9,9, Direction.DOWN));
        assertFalse(board.canPlay(letters, 9,9, Direction.RIGHT));

        board.playWord(letters, 7, 7, Direction.RIGHT);
        assertTrue(board.canPlay(letters, 1,14, Direction.DOWN));
        assertFalse(board.canPlay(letters, 1,13, Direction.DOWN));
    }

    @Test
    void testScoreWord() {
        letters.clear();
        assertTrue(letters.isEmpty());
        letters.add(B);
        letters.add(A);
        letters.add(Z);
        assertEquals(letters.size(), 3);
        //TW
        assertEquals((3 + 1 + 10) * 3, board.scoreWord(letters, 0,0,Direction.DOWN));
        board = new Board();
        assertEquals((3 + 1 + 10) * 3, board.scoreWord(letters, 0,0,Direction.RIGHT));
        board = new Board();
        // DW
        assertEquals((3 + 1 + 10) * 2, board.scoreWord(letters, 1,1,Direction.DOWN));
        board = new Board();
        assertEquals((3 + 1 + 10) * 2, board.scoreWord(letters, 1,1,Direction.DOWN));
        board = new Board();
        //Normal
        assertEquals((3 + 1 + 10), board.scoreWord(letters, 1,2,Direction.RIGHT));
        board = new Board();
        //TL
        assertEquals((3 * 3 + 1 + 10), board.scoreWord(letters, 1,5,Direction.RIGHT));
        board = new Board();
        assertEquals((3 + 1 + 10 * 3), board.scoreWord(letters, 3,5,Direction.DOWN));
        board = new Board();
        //DL
        assertEquals(3 + 1 * 2 + 10, board.scoreWord(letters, 10,14,Direction.DOWN));
        board = new Board();
        
    }

    @Test
    void testGetNumCharOnBoardNoLettersPlaced() {
        Map<Character,Integer> counts = board.getNumEachCharOnBoard();
        // Every board position should be a board tile, 
        // so the Map will be empty, since nothing is put into it since
        // the loop continues if it views a board tile.
        assertTrue(counts.values().isEmpty());
        assertTrue(counts.keySet().isEmpty());
    }

    @Test
    void testGetNumCharOnBoardOneTilePlaced() {
        List<LetterTile> toAdd = new ArrayList<>();
        // Add lettertile B to toAdd
        toAdd.add(B);
        // Play word "B" at 7,7 (direction is arbitrary)
        board.playWord(toAdd,7,7, Direction.DOWN);
        // Now our Map shouldnt be empty
        Map<Character,Integer> counts = board.getNumEachCharOnBoard();

        // one key and one value, both for B
        assertEquals(1, counts.values().size());
        assertEquals(1, counts.keySet().size());

        // Check they're the right entries
        assertEquals(counts.get('B'), 1);
    }

    @Test
    void testGetNumCharOnBoardFewWordsPlaced() {
        List<LetterTile> toAdd = new ArrayList<>();
        List<LetterTile> toAdd2 = new ArrayList<>();
        // Add lettertile B to toAdd
        toAdd.add(A);
        toAdd.add(Z);
        // Play word "AZ" at 7,7 ; 7,8
        board.playWord(toAdd,7,7, Direction.RIGHT);
        toAdd2.add(B);
        board.playWord(toAdd2, 0,0, Direction.DOWN);
        // Now our Map should have A, B, Z keys
        Map<Character,Integer> counts = board.getNumEachCharOnBoard();

        // one key and one value, both for B
        assertEquals(3, counts.values().size());
        assertEquals(3, counts.keySet().size());

        // Check they're the right entries
        assertEquals(counts.get('A'), 1);
        assertEquals(counts.get('B'), 1);
        assertEquals(counts.get('Z'), 1);
        // didn't add d so it should not be in the map
        assertEquals(counts.get('D'), null);
    }

    @Test
    void testGetNumCharOnBoardSameLetterAddedMoreThanOnce() {
        LetterTile A2 = new LetterTile('A', 1);
        List<LetterTile> toAdd = new ArrayList<>();
        List<LetterTile> toAdd2 = new ArrayList<>();
        // Add lettertile B to toAdd
        toAdd.add(A);
        toAdd.add(A2);
        toAdd.add(Z);
        // Play word "AZ" at 7,7 ; 7,8
        board.playWord(toAdd,6,8, Direction.RIGHT);
        toAdd2.add(B);
        board.playWord(toAdd2, 0,0, Direction.DOWN);
        // Now our Map should have A, B, Z keys
        Map<Character,Integer> counts = board.getNumEachCharOnBoard();

        // one key and one value, both for B
        assertEquals(3, counts.values().size());
        assertEquals(3, counts.keySet().size());

        // Check they're the right entries
        assertEquals(counts.get('A'), 2);
        assertEquals(counts.get('B'), 1);
        assertEquals(counts.get('Z'), 1);
        // didn't add d so it should not be in the map
        assertEquals(counts.get('_'), null);
    }
    
    @Test
    void testGetTileAtPositionOnBoard() {
        BoardTile boardTile = (BoardTile) board.getTileAtPositionOnBoard(7,7);
        assertEquals(boardTile.getTileType(), TileType.DOUBLE_WORD);


        BoardTile twTile = (BoardTile) board.getTileAtPositionOnBoard(7,0);
        assertEquals(twTile.getTileType(), TileType.TRIPLE_WORD);

        BoardTile tLTile = (BoardTile) board.getTileAtPositionOnBoard(1,5);
        assertEquals(tLTile.getTileType(), TileType.TRIPLE_LETTER);

        BoardTile dLTile = (BoardTile) board.getTileAtPositionOnBoard(0,3);
        assertEquals(dLTile.getTileType(), TileType.DOUBLE_LETTER);
    }

}
