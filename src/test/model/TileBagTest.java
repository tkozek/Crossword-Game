package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;


public class TileBagTest {
    private TileBag testBag;
    private Set<Character> set;
    private Player testPlayer;
    private Player testPlayer2;
    private Board testBoard;
    private HashSet<Character> chars;
    @BeforeEach
    void runBefore() {
         testBag = new TileBag();
         testBoard = new Board();
         testPlayer = new Player("tester", testBoard, testBag);
         testPlayer2 = new Player("otherTester", testBoard,testBag);
         
    }

    @Test
    void testConstructor() {
        //100 tiles initially
        assertEquals(testBag.numTilesRemaining(), 100);
    }

    @Test
    void testDrawTileStartGameCase() {
        //0 tiles on rack initially
        assertEquals(testPlayer.getNumTilesOnRack(),0);
        //No tiles removed yet
        assertEquals(testBag.numTilesRemaining(), TileBag.TOTAL_LETTERS_INITIALLY);
        // Enough tiles removed from bag to fill player's rack
        assertEquals(testBag.drawTiles(testPlayer), Player.MAX_NUM_TILES);
        //Enough tiles removed from bag to fill player's rack
        assertEquals(testBag.numTilesRemaining(), TileBag.TOTAL_LETTERS_INITIALLY -  Player.MAX_NUM_TILES);
        // MAX_NUM_TILES get added to rack
        assertEquals(testPlayer.getNumTilesOnRack(), Player.MAX_NUM_TILES);
        //Cannot draw more tiles 
        assertEquals(0, testBag.drawTiles(testPlayer));
    }

/*     will test through playe
    @Test
    void testNotAllTheSameTile() {
        testBag.drawTiles(testPlayer);
        testBag.drawTiles(testPlayer2);
        
        List<LetterTile> p1Letters = testPlayer.getTilesOnRack();
        List<LetterTile> p2Letters = testPlayer2.getTilesOnRack();
        p1Letters.addAll(p2Letters);
        assertEquals(p1Letters.size(), 14);
        chars = new HashSet<>(p1Letters);
        // Most occurent character is E with 12 occurences in a new game
        // so 14 tiles must have at least two unique characters.
        assertTrue(chars.size() >= 2);

    } */
    @Test
    void testEmptyDrawPile() {
        testBag.emptyDrawPile();
        assertEquals(0, testBag.numTilesRemaining());
        assertEquals(-1, testBag.drawTiles(testPlayer));

    }
}

