package model.board;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.tile.TileType;



public class BoardTileTest {

    BoardTile doubleWordTile;
    BoardTile doubleLetterTile;
    BoardTile tripleWordTile;
    BoardTile tripleLetterTile;
    BoardTile regularTile;

    @BeforeEach
    void runBefore() {
        doubleWordTile = new BoardTile(1,1,TileType.DOUBLE_WORD);
        tripleWordTile = new BoardTile(0,0,TileType.TRIPLE_WORD);
        doubleLetterTile = new BoardTile(0,3,TileType.DOUBLE_LETTER);
        tripleLetterTile = new BoardTile(5,1,TileType.TRIPLE_LETTER);
        regularTile = new BoardTile(0,1, TileType.NORMAL);
    }

    @Test
    void testConstructor() {


        assertTrue(doubleLetterTile.checkIsTileType(TileType.DOUBLE_LETTER));
        assertFalse(doubleLetterTile.checkIsTileType(TileType.TRIPLE_WORD));
        
        assertEquals(1,doubleWordTile.getRow());
        assertEquals(1,doubleWordTile.getCol());

        assertTrue(tripleWordTile.checkIsTileType(TileType.TRIPLE_WORD));
        
        assertEquals(0,tripleWordTile.getRow());
        assertEquals(0,tripleWordTile.getCol());


        assertTrue(tripleLetterTile.checkIsTileType(TileType.TRIPLE_LETTER));
        assertFalse(doubleLetterTile.checkIsTileType(TileType.TRIPLE_WORD));
       
        assertEquals(0,doubleLetterTile.getRow());
        assertEquals(3,doubleLetterTile.getCol());

    }

    @Test
    void testToDisplay() {
        assertEquals("DWS", doubleWordTile.toDisplay());
        assertEquals("TWS", tripleWordTile.toDisplay());
        assertEquals("DLS", doubleLetterTile.toDisplay());
        assertEquals("TLS", tripleLetterTile.toDisplay());
        assertEquals(" ", regularTile.toDisplay());
    }

    @Test
    void testGetTerminalPrintoutString() {
        assertEquals("DWS| ", doubleWordTile.getTerminalPrintoutString());
        assertEquals("TWS| ", tripleWordTile.getTerminalPrintoutString());
        assertEquals("DLS| ", doubleLetterTile.getTerminalPrintoutString());
        assertEquals("TLS| ", tripleLetterTile.getTerminalPrintoutString());
        assertEquals("___| ", regularTile.getTerminalPrintoutString());
    }

    @Test
    void testOccupiesBoardSpot() {
        assertFalse(doubleWordTile.occupiesBoardSpot());
        assertFalse(tripleWordTile.occupiesBoardSpot());
        assertFalse(doubleLetterTile.occupiesBoardSpot());
        assertFalse(tripleLetterTile.occupiesBoardSpot());
        assertFalse(regularTile.occupiesBoardSpot());
    }

    @Test
    void testGetPoints() {
        assertEquals(0, doubleWordTile.getPoints());
        assertEquals(0, tripleWordTile.getPoints());
        assertEquals(0, doubleLetterTile.getPoints());
        assertEquals(0, tripleLetterTile.getPoints());
        assertEquals(0, regularTile.getPoints());
    }
    
    @Test
    void testGetTileTypeAllTypes() {
        assertEquals(doubleWordTile.getTileType(), TileType.DOUBLE_WORD);
        assertEquals(doubleLetterTile.getTileType(), TileType.DOUBLE_LETTER);
        assertEquals(tripleLetterTile.getTileType(), TileType.TRIPLE_LETTER);
        assertEquals(tripleWordTile.getTileType(), TileType.TRIPLE_WORD);
    }
}

