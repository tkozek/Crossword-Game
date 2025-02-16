package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class BoardTest {
    
    private Board board;
    private TileBag testBag;
    private Set<Character> set;
    private Player testPlayer;
    private Player testPlayer2;
    private Board testBoard;
    private HashMap<Character, Integer> preSwapChars;
    private HashMap<Character, Integer> postSwapChars;
    private List<LetterTile>  letters;

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        testPlayer = new Player("Trevor", testBoard,testBag);
        testPlayer2 = new Player("Rovert", testBoard,testBag);
        preSwapChars = new HashMap<>();
        postSwapChars = new HashMap<>();
        letters = testPlayer.getTilesOnRack();
    }
    
    @Test
    void testSectionContainsWithAllTypes() {
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.DOWN, TileType.DOUBLE_LETTER));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.DOWN, TileType.TRIPLE_WORD));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.RIGHT, TileType.DOUBLE_WORD));
        assertTrue(board.sectionContainsTileType(letters,1,0,Direction.RIGHT, TileType.TRIPLE_LETTER));
    }
    @Test
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

    }


    @Test 
    public void testSquareIsAvailable() {
        assertTrue(board.squareIsAvailable(0, 0));
        assertTrue(board.squareIsAvailable(14, 14));
        assertTrue(board.squareIsAvailable(7, 7));
        assertTrue(board.squareIsAvailable(8, 1));
        assertTrue(board.squareIsAvailable(1, 13));

    }

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
        //
        assertTrue(board.inBounds(letters, 7,3, Direction.RIGHT));
        assertTrue(board.inBounds(letters, 7,2, Direction.RIGHT));
        assertTrue(board.inBounds(letters, 7,1, Direction.RIGHT));

        assertTrue(board.inBounds(letters, 14,7, Direction.RIGHT));
        assertTrue(board.inBounds(letters, 14,8, Direction.RIGHT));
        assertFalse(board.inBounds(letters, 14,9, Direction.RIGHT));
        assertFalse(board.inBounds(letters, 14,10, Direction.RIGHT));

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
    void testPlayWord() {
        testBag.drawTiles(testPlayer);
        // no moves played yet
        assertEquals(testPlayer.getHistory().getMoves().size(),0);

        // Center square as if first turn
        assertTrue(board.squareIsAvailable(7,7));
        assertTrue(board.squareIsAvailable(7,8));
        //2nd rack tile placed first, 1st rack tile placed second
        testPlayer.selectTile(1);
        testPlayer.selectTile(0);
        List<LetterTile> selectedTiles = testPlayer.getSelectedTiles();
        LetterTile tile1 = selectedTiles.get(0);
        LetterTile tile2 = selectedTiles.get(1);
        assertEquals(tile1, testPlayer.getTilesOnRack().get(1));
        assertEquals(tile2, testPlayer.getTilesOnRack().get(0));

        assertTrue(board.squareisTileType(7,7,TileType.DOUBLE_WORD));
        int pointsGained = board.playWord(selectedTiles,7,7, Direction.RIGHT);

        assertEquals(board[7][7].equals(tile1));
        assertEquals(board[7][8].equals(tile2));
        //Double word score at center of board
        assertEquals(pointsGained, 2* (tile1.getLetterPoints() + tile2.getLetterPoints()));
        //assertEquals(testPlayer.getPointsThisGame(), pointsGained);
        // Selected tiles should be removed from rack once played
        //assertTrue(testPlayer.getSelectedTiles().isEmpty());
        //New move should be added
        //assertEquals(testPlayer.getHistory().getMoves().size(),1);
    }
}
