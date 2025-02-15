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
    

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        board =  new Board();
        testPlayer = new Player("Trevor", testBoard,testBag);
        testPlayer2 = new Player("Rovert", testBoard,testBag);
        preSwapChars = new HashMap<>();
        postSwapChars = new HashMap<>();
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
        int pointsGained = board.playWord(selectedTiles,7,7, Direction.RIGHT);

        assertEquals(board[7][7].equals(tile1));
        assertEquals(board[7][8].equals(tile2));
        //Double word score at center of board
        assertEquals(pointsGained, 2* (tile1.getLetterPoints() + tile2.getLetterPoints()));
        assertEquals(testPlayer.getPointsThisGame(), pointsGained);
        // Selected tiles should be removed from rack once played
        assertTrue(testPlayer.getSelectedTiles().isEmpty());
        //New move should be added
        assertEquals(testPlayer.getHistory().getMoves().size(),1);


    }
}
