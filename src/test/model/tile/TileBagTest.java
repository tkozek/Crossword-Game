package model.tile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Player;
import model.ScrabbleGame;



public class TileBagTest {
    
    private static final int NUM_TILES_TILEBAG_INITIALLY = TileBag.TOTAL_LETTERS_INITIALLY;
    private static final int MAX_NUM_TILES = TileBag.MAX_NUM_PLAYER_TILES;
    private Player testPlayer;
    private ScrabbleGame game;
    private LetterTile b1;
    private LetterTile e1;
    private LetterTile b2;

    @BeforeEach
    public void runBefore() {
        game = new ScrabbleGame();
        testPlayer = new Player("tester");
        b1 = new LetterTile('B', 3);
        b2 = new LetterTile('B', 3);
        e1 = new LetterTile('E', 1);
    }

    @Test
    public void testConstructor() {
        //100 tiles initially
        assertEquals(game.getTileBag().numTilesRemaining(), 100);
    }

    @Test
    public void testDrawTileStartGameCase() {
        //0 tiles on rack initially
        assertEquals(testPlayer.getNumTilesOnRack(),0);
        //No tiles removed yet
        assertEquals(game.getTileBag().numTilesRemaining(), NUM_TILES_TILEBAG_INITIALLY);
        // Enough tiles removed from bag to fill player's rack
        assertEquals(game.getTileBag().drawTiles(testPlayer), MAX_NUM_TILES);
        //Enough tiles removed from bag to fill player's rack
        assertEquals(game.getTileBag().numTilesRemaining(), NUM_TILES_TILEBAG_INITIALLY -  MAX_NUM_TILES);
        // MAX_NUM_TILES get added to rack
        assertEquals(testPlayer.getNumTilesOnRack(), MAX_NUM_TILES);
        //Cannot draw more tiles 
        assertEquals(0, game.getTileBag().drawTiles(testPlayer));
    }

    @Test
    public void testEmptyDrawPile() {
        game.getTileBag().emptyDrawPile();
        assertEquals(0, game.getTileBag().numTilesRemaining());
        assertEquals(-1, game.getTileBag().drawTiles(testPlayer));

    }
    
    @Test
    public void testDrawTilesEmptyPile() {
        game.getTileBag().emptyDrawPile();
        LetterTile letter = new LetterTile('A', 1);
        game.getTileBag().addTile(letter);
        assertEquals(0, testPlayer.getNumTilesOnRack());
        // the single A we added will be drawn, then 
        // loop stops executing since the pile is empty, even though Player still 
        // doesnt have a full rack
        assertEquals(game.getTileBag().drawTiles(testPlayer), 1);
    }

    @Test
    public void testGetCurrentLetterFrequencies() {
        game.getTileBag().emptyDrawPile();
        assertTrue(game.getTileBag().getCurrentLetterFrequencies().keySet().isEmpty());
        assertTrue(game.getTileBag().getCurrentLetterFrequencies().values().isEmpty());
        game.getTileBag().addTile(b1);
        assertFalse(game.getTileBag().getCurrentLetterFrequencies().keySet().isEmpty());
        assertEquals(1, game.getTileBag().getCurrentLetterFrequencies().get('B'));
        game.getTileBag().addTile(e1);
        assertEquals(2, game.getTileBag().getCurrentLetterFrequencies().size());
        assertEquals(1, game.getTileBag().getCurrentLetterFrequencies().get('E'));
        game.getTileBag().addTile(b2);
        assertEquals(2, game.getTileBag().getCurrentLetterFrequencies().get('B'));
    }
}