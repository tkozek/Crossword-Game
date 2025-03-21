package model.tile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Player;
import model.ScrabbleGame;
import model.board.Board;



public class TileBagTest {
    private TileBag testBag;
    private Player testPlayer;
    private Board testBoard;
    private ScrabbleGame game;
    private LetterTile b1;
    private LetterTile e1;
    private LetterTile b2;

    @BeforeEach
    void runBefore() {
        testBag = new TileBag();
        testBoard = new Board();
        game = new ScrabbleGame("alphabet", testBoard, testBag);
        testPlayer = new Player("tester", game);
        b1 = new LetterTile('B', 3);
        b2 = new LetterTile('B', 3);
        e1 = new LetterTile('E', 1);
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
        assertEquals(testBag.drawTiles(testPlayer), TileBag.MAX_NUM_PLAYER_TILES);
        //Enough tiles removed from bag to fill player's rack
        assertEquals(testBag.numTilesRemaining(), TileBag.TOTAL_LETTERS_INITIALLY -  TileBag.MAX_NUM_PLAYER_TILES);
        // MAX_NUM_TILES get added to rack
        assertEquals(testPlayer.getNumTilesOnRack(), TileBag.MAX_NUM_PLAYER_TILES);
        //Cannot draw more tiles 
        assertEquals(0, testBag.drawTiles(testPlayer));
    }

/*     will test through player
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
    
    @Test
    void testDrawTilesEmptyPile() {
        testBag.emptyDrawPile();
        LetterTile letter = new LetterTile('A', 1);
        testBag.addTile(letter);
        assertTrue(testPlayer.getNumTilesOnRack() + 1 < TileBag.MAX_NUM_PLAYER_TILES);
        // the single A we added will be drawn, then 
        // loop stops executing since the pile is empty, even though Player still 
        // doesnt have a full rack
        assertEquals(testBag.drawTiles(testPlayer), 1);
    }

    @Test
    void testGetCurrentLetterFrequencies() {
        testBag.emptyDrawPile();
        assertTrue(testBag.getCurrentLetterFrequencies().keySet().isEmpty());
        assertTrue(testBag.getCurrentLetterFrequencies().values().isEmpty());
        testBag.addTile(b1);
        assertFalse(testBag.getCurrentLetterFrequencies().keySet().isEmpty());
        assertEquals(1, testBag.getCurrentLetterFrequencies().get('B'));
        testBag.addTile(e1);
        assertEquals(2, testBag.getCurrentLetterFrequencies().size());
        assertEquals(1, testBag.getCurrentLetterFrequencies().get('E'));
        testBag.addTile(b2);
        assertEquals(2, testBag.getCurrentLetterFrequencies().get('B'));
    }
}

